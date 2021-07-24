package com.smilegate.worksmile.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.smilegate.worksmile.common.PreferenceUtil
import com.smilegate.worksmile.model.BaseItem
import com.smilegate.worksmile.model.Title
import com.smilegate.worksmile.model.chat.ChatMessage
import com.smilegate.worksmile.model.chatroom.DeleteRoomReqResp
import com.smilegate.worksmile.model.chatroom.RoomIdUserIdReq
import com.smilegate.worksmile.network.ApiService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompHeader

class RoomListViewModel(application: Application) : BaseViewModel(application) {
    private val mDisposable: CompositeDisposable = CompositeDisposable()
    private val mUserMessage = MutableLiveData<ChatMessage>()
    private val mRoomList = MutableLiveData<List<BaseItem>>()

    private var mStomp: StompClient

    val userMessage: LiveData<ChatMessage>
        get() = mUserMessage

    val roomList: LiveData<List<BaseItem>>
        get() = mRoomList

    init {
        val url = "ws://52.198.41.19:8080/ws/websocket"
        val stompHeaders = mutableListOf(
            StompHeader("X-Auth-Token", "xxx"),
            StompHeader("uid", PreferenceUtil.userId)
        )
        mStomp = Stomp.over(
            Stomp.ConnectionProvider.OKHTTP,
            url
        )
        mStomp.connect(stompHeaders)
    }

    fun startSenderSub(sender: String) {
        mStomp.run {
            mDisposable.addAll(
                topic("/sub/msg/user/$sender")
                    .map {
                        Gson().fromJson(it.payload, ChatMessage::class.java)
                    }
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        mUserMessage.postValue(it)
                    }, {
                        Log.d("SYH", "SenderSub Throwable : ${it.message}")
                    })
            )
        }
    }

    @SuppressLint("CheckResult")
    fun getRooms() {
        ApiService.roomApi.getRoomList(mUserId)
            .map { resp ->
                val items = mutableListOf<BaseItem>()
                val data = resp.getDataOrThrowable()
                val roomList = data.rooms
                val lastMsgList = data.lastMessages

                lastMsgList.forEach { chat ->
                    roomList.firstOrNull { it.roomId == chat.ruuid }?.let {
                        chat.massageList[0].run {
                            it.lastMessage = content.toString()
                            it.lastMessageTime = created_at
                            it.unreadMessageCnt = (this.midx ?: 0L) - it.lastReadIdx
                        }
                    }
                }

                val favoriteRoomList = roomList.filter { it.isFavorite }
                val commonRoomList = roomList.filter { !it.isFavorite }

                if (favoriteRoomList.isNotEmpty()) {
                    items.add(Title("즐겨찾기"))
                    items.addAll(favoriteRoomList)
                }

                if (commonRoomList.isNotEmpty()) {
                    items.add(Title("최근"))
                    items.addAll(commonRoomList)
                }

                return@map items.toList()
            }
            .subscribeOn(Schedulers.io())
            .subscribe({
                mRoomList.postValue(it)
            }, {
                mMessage.postValue(it.localizedMessage)
            })
    }

    @SuppressLint("CheckResult")
    fun deleteRoom(roomId: String) {
        ApiService.roomApi.deleteRoom(DeleteRoomReqResp(roomId))
            .map { it.getDataOrThrowable().roomId.isNotBlank() }
            .filter { it }
            .subscribeOn(Schedulers.io())
            .subscribe({
                mMessage.postValue("채팅방이 삭제되었습니다.")
                getRooms()
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
        }.filter { it }.subscribeOn(Schedulers.io())
            .subscribe({
                mMessage.postValue("채팅방에서 나왔습니다.")
                getRooms()
            }, {
                mMessage.postValue(it.localizedMessage)
            })
    }

    override fun onCleared() {
        mStomp.disconnect()

        if (!mDisposable.isDisposed) {
            mDisposable.dispose()
        }

        super.onCleared()
    }
}