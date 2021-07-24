package com.smilegate.worksmile.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.toLiveData
import com.google.gson.Gson
import com.smilegate.worksmile.common.PreferenceUtil
import com.smilegate.worksmile.model.chat.MessageType
import com.smilegate.worksmile.model.User
import com.smilegate.worksmile.model.chat.ChatMessage
import com.smilegate.worksmile.model.chat.SystemMessageContent
import com.smilegate.worksmile.model.chatroom.*
import com.smilegate.worksmile.network.ApiService
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Singles
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ChatViewModel(application: Application) : BaseViewModel(application) {
    private val mDisposable: CompositeDisposable = CompositeDisposable()
    private val mMessageListData = MutableLiveData<List<ChatRoomItem>>()
    private val mRoomMessage = MutableLiveData<ChatMessage>()
    private val mRoomInfo = MutableLiveData<ChatRoom>()
    private val mExitRoom = MutableLiveData<Boolean>()
    private val mMessageSearchMode = MutableLiveData<Boolean>()
    private val mKeywordMessageId = MutableLiveData<Long>()
    private val mNotice = MutableLiveData<String>()

    private val mKeywordMessageIdList = mutableListOf<Long>()
    private val mMessageList = mutableListOf<ChatMessage>()
    private val mUserLastMessageIndexMap: MutableMap<String, Long> = mutableMapOf()

    private val mSubLastMessageIndex = BehaviorSubject.create<Map<String, Long>>()

    private val mStomp: StompClient by lazy {
        val url = "ws://52.198.41.19:8080/ws/websocket"
        val stompHeaders = mutableListOf(
            StompHeader("X-Auth-Token", "xxx"),
            StompHeader("uid", mUserId)
        )
        val stomp = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            url
        )

        stomp.connect(stompHeaders)
        stomp
    }

    val roomMessage: LiveData<ChatMessage> = mRoomMessage
    val roomMessageList: LiveData<List<ChatRoomItem>> = mMessageListData
    val roomInfo: LiveData<ChatRoom> = mRoomInfo
    val exitRoom: LiveData<Boolean> = mExitRoom
    val stompEvent: LiveData<LifecycleEvent> = mStomp.lifecycle().toLiveData()
    val messageSearchMode: LiveData<Boolean> = mMessageSearchMode
    val keywordMessageId: LiveData<Long> = mKeywordMessageId
    val notice: LiveData<String> = mNotice

    init {
        mDisposable.add(mSubLastMessageIndex.map { messageIndexMap ->
            messageIndexMap.keys.forEach { key ->
                mUserLastMessageIndexMap[key] = messageIndexMap[key] ?: 0
            }

            mUserLastMessageIndexMap
        }.distinctUntilChanged().map { lastMessageIndexMap ->
            val lastMessageIndexList = lastMessageIndexMap.map { it.value }

            mMessageList.map { message ->
                message.unReadCount = lastMessageIndexList.count { it < message.midx ?: 0 }
                message
            }.sortedBy { it.midx }
        }.distinctUntilChanged().map {
            getChatListItems(it)
        }.subscribeOn(Schedulers.io()).subscribe({
            mMessageListData.postValue(it)
        }, {
        }))
    }

    @SuppressLint("CheckResult")
    fun getRoomInfo(roomId: String) {
        ApiService.roomApi.getRoomInfo(roomId, RoomIdUserIdReq(userId = mUserId))
            .map { it.getDataOrThrowable().room }
            .subscribeOn(Schedulers.io())
            .subscribe({
                mRoomInfo.postValue(it)
            }, {
                mMessage.postValue(it.localizedMessage)
            })
    }

    @SuppressLint("CheckResult")
    fun inviteRoomMembers(roomId: String, userIdList: List<String>) {
        ApiService.roomApi.getMemberList(roomId)
            .map { it.getDataOrThrowable().users }
            .map { roomMembers ->
                val userIdSet = userIdList.toMutableSet()
                userIdSet.removeAll(roomMembers.map { it.uid })
                return@map userIdSet.toList()
            }
            .flatMap { inviteUserIdList ->
                ApiService.roomApi.inviteMemberList(
                    InviteRoomUsersReq(
                        roomId = roomId,
                        users = inviteUserIdList
                    )
                ).map { it.getDataOrThrowable() }
            }
            .filter { it.users.isNotEmpty() }
            .subscribeOn(Schedulers.io())
            .subscribe({
                mMessage.postValue("멤버를 초대하였습니다.")
                getRoomInfo(roomId)
            }, {})
    }

    private var mUserList = emptyList<User>()

    @SuppressLint("CheckResult")
    fun getPreviousMessages(roomId: String) {
        Singles.zip(
            ApiService.previousMessageApi.getPreviousMessageList(roomId, 0L, 100)
                .map { resp -> resp.getDataOrThrowable().messages.filter { it.isValidMessage } },
            ApiService.roomApi.getMemberList(roomId)
                .map { it.getDataOrThrowable().users }
        ).map { pair ->
            val messages: List<ChatMessage> = pair.first.sortedBy { it.midx }
            val lastMessageIndexMap = mutableMapOf<String, Long>()
            val userList: List<User> = pair.second.orEmpty().also {
                mUserList = it
            }

            userList.forEach {
                lastMessageIndexMap[it.uid] = it.lastReadIdx ?: 0L
            }

            lastMessageIndexMap[mUserId] = messages.lastOrNull { it.isValidMessage }?.midx ?: 0

            mSubLastMessageIndex.onNext(lastMessageIndexMap)

            messages.forEach { chatMessage ->
                chatMessage.userThumbnail =
                    userList.firstOrNull { user -> user.uid == chatMessage.sender }?.profile
                chatMessage.unReadCount =
                    userList.count { user ->
                        user.uid != mUserId && (user.lastReadIdx ?: 0) < (chatMessage.midx ?: 0)
                    }

                chatMessage.parent_id?.takeIf { it > 0 }?.let { id ->
                    chatMessage.parentMessageContent =
                        messages.firstOrNull { it.midx == id }?.content.toString()
                }
            }

            return@map messages.also {
                mMessageList.run {
                    clear()
                    addAll(it)
                }
            }
        }.map {
            getChatListItems(it)
        }.subscribeOn(Schedulers.io()).doFinally {
            startRoomSub(roomId)
        }.subscribe({
            mMessageListData.postValue(it)
        }, {
            mMessageListData.postValue(emptyList())
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun getChatListItems(messageList: List<ChatMessage>): List<ChatRoomItem> {
        val chatListItem = mutableListOf<ChatRoomItem>()

        messageList.sortedBy { it.midx }.groupBy { chatMessage ->
            chatMessage.date?.let {
                SimpleDateFormat("yyyy년 M월 d일").format(it)
            } ?: ""
        }.let { map ->
            map.keys.forEach { date ->
                chatListItem.run {
                    map[date]?.takeIf { it.isNotEmpty() }?.let {
                        add(DateDivider(date))
                        addAll(it)
                    }
                }
            }
        }

        return chatListItem
    }

    @SuppressLint("CheckResult")
    private fun startRoomSub(roomId: String) {
        mStomp.topic("/sub/msg/room/$roomId")
            .map {
                Log.d("SYH", "Room Message : ${it.payload}")
                Gson().fromJson(it.payload, ChatMessage::class.java)
            }
            .doOnNext { chatMessage ->
                when {
                    chatMessage.type == MessageType.ON -> {
                        val systemMessageContent =
                            Gson().fromJson(
                                chatMessage.content.toString(),
                                SystemMessageContent::class.java
                            )

                        val lastMessageIndex = mMessageList.lastOrNull { it.isValidMessage }?.midx
                            ?: systemMessageContent.lastReadIdx ?: 0

                        val lastMessageIndexMap = mutableMapOf<String, Long>()
                        systemMessageContent.onUserList.forEach {
                            lastMessageIndexMap[it] = lastMessageIndex
                        }

                        mSubLastMessageIndex.onNext(lastMessageIndexMap)
                    }
                    chatMessage.isValidMessage -> {
                        val messageIndex = chatMessage.midx ?: 0
                        mUserLastMessageIndexMap[mUserId] = messageIndex

                        chatMessage.run {
                            userThumbnail =
                                mUserList.firstOrNull { user -> user.uid == sender }?.profile
                            unReadCount = mUserLastMessageIndexMap.map { it.value }
                                .count { messageIndex > it }
                        }

                        mMessageList.firstOrNull { it.midx == chatMessage.parent_id }?.let {
                            chatMessage.parentMessageContent = it.content.toString()
                        }
                    }
                    else -> {
                    }
                }
            }
            .filter { it.isValidMessage }
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.type == MessageType.NOTIFY) {
                    mNotice.postValue(it.content.toString())
                }

                mRoomMessage.postValue(it)
            }, {
                Log.d("SYH", "RoomSub Throwable : ${it.message}")
            }).addDisposable()
    }

    @SuppressLint("CheckResult")
    fun sendMessage(
        sender: String,
        roomId: String,
        content: Any? = null,
        type: MessageType,
        parentMessageId: Long? = null
    ) {
        mStomp.send(
            "/pub/msg", Gson().toJson(
                ChatMessage(
                    ruuid = roomId,
                    sender = sender,
                    type = type,
                    content = content ?: "",
                    device = "app",
                    parent_id = parentMessageId
                )
            )
        ).subscribeOn(Schedulers.io()).subscribe({
            Log.d("SYH", "Message Sent")
        }, {
            mMessage.postValue(it.localizedMessage)
        })
    }

    @SuppressLint("CheckResult")
    fun exitRoom(roomId: String) {
        ApiService.roomApi.exitRoom(
            RoomIdUserIdReq(
                userId = mUserId,
                roomId = roomId
            )
        ).map {
            it.getDataOrThrowable().user.uid == mUserId
        }.filter { it }.subscribeOn(Schedulers.io()).subscribe({
            sendMessage(PreferenceUtil.userId, roomId, type = MessageType.EXIT)

            mMessage.postValue("채팅방에서 나왔습니다.")
            mExitRoom.postValue(true)
        }, {
            mMessage.postValue(it.localizedMessage)
        })
    }

    @SuppressLint("CheckResult")
    fun uploadFile(roomId: String, type: String, fileUri: Uri) {
        Single.fromCallable {
            val realPath = getRealPathFromUri(fileUri)
            val file = File(realPath)

            val requestFile: RequestBody = RequestBody.create(
                context.contentResolver.getType(Uri.parse(file.absolutePath)).orEmpty()
                    .toMediaTypeOrNull(),
                file
            )

            return@fromCallable MultipartBody.Builder().run {
                addFormDataPart("uid", mUserId)
                addFormDataPart("type", type)
                addFormDataPart("file", file.name, requestFile)
            }.build().parts
        }.flatMap { part ->
            ApiService.roomApi.updateFile(part).map {
                it.getDataOrThrowable().file
            }
        }.subscribeOn(Schedulers.io()).subscribe({
            sendMessage(
                mUserId,
                roomId,
                it.fileUrl,
                type = MessageType.IMAGE
            )
            mMessage.postValue("${it.fileName} 업로드를 성공하였습니다.")
            Log.d("SYH", "Image Upload url ${it.fileUrl}")
        }, {
            mMessage.postValue(it.localizedMessage)
        })
    }

    fun setMessageSearchMode(isSearching: Boolean) {
        mMessageSearchMode.value = isSearching
    }

    @SuppressLint("CheckResult")
    fun setMessageSearchKeyword(keyword: String?) {
        Single.fromCallable {
            mMessageList.forEach { message ->
                val searchKeyword =
                    if (message.content.toString().contains(keyword.orEmpty())) keyword else null
                message.keyword = searchKeyword

                if (searchKeyword != null) {
                    message.midx?.let {
                        mKeywordMessageIdList.add(it)
                    }
                }
            }

            return@fromCallable getChatListItems(mMessageList)
        }.doOnSubscribe {
            mKeywordMessageIdList.clear()
            mKeywordMessageId.postValue(0)
        }.subscribeOn(Schedulers.computation()).subscribe({ items ->
            mMessageListData.postValue(items)

            mKeywordMessageIdList.lastOrNull()?.let {
                mKeywordMessageId.postValue(it)
            }
        }, {})
    }

    fun moveToPreviousSearchMessage() {
        mKeywordMessageIdList.indexOfFirst { it == mKeywordMessageId.value }.takeIf { it > 0 }
            ?.let {
                mKeywordMessageId.value = mKeywordMessageIdList[it - 1]
            }
    }

    fun moveToNextSearchMessage() {
        mKeywordMessageIdList.indexOfFirst { it == mKeywordMessageId.value }
            .takeIf { it < mKeywordMessageIdList.lastIndex }
            ?.let {
                mKeywordMessageId.value = mKeywordMessageIdList[it + 1]
            }
    }

    private fun getRealPathFromUri(contentUri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            cursor = context.contentResolver.query(
                contentUri,
                arrayOf(MediaStore.Images.Media.DATA),
                null,
                null,
                null
            )
            cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)?.let {
                cursor.moveToFirst()
                cursor.getString(it)
            }
        } finally {
            cursor?.close()
        }
    }

    override fun onCleared() {
        super.onCleared()

        mStomp.disconnect()
    }
}