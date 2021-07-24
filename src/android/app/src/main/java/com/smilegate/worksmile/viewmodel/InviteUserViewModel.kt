package com.smilegate.worksmile.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.smilegate.worksmile.model.User
import com.smilegate.worksmile.model.chatroom.ChatRoom
import com.smilegate.worksmile.model.chatroom.CreateRoomReq
import com.smilegate.worksmile.network.ApiService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class InviteUserViewModel(application: Application) : BaseViewModel(application) {
    private val mUserList = MutableLiveData<List<User>>()
    private val mInvitedUserSet = MutableLiveData<Set<User>>()
    private val mCreatedRoom = MutableLiveData<Pair<ChatRoom, List<String>>>()

    private val mSubKeyword = BehaviorSubject.createDefault("")

    val userList: LiveData<List<User>> = mUserList

    val invitedUserSet: LiveData<Set<User>> = mInvitedUserSet

    val createChatRoomEnabled: LiveData<Boolean> = mInvitedUserSet.map {
        it.isNotEmpty()
    }

    val createdRoom: LiveData<Pair<ChatRoom, List<String>>> = mCreatedRoom

    init {
        mSubKeyword.debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .subscribe({
                searchUserList(it)
            }, {
                searchUserList("")
            }).addDisposable()
    }

    fun setKeyword(keyword: String) {
        mSubKeyword.onNext(keyword)
    }

    @SuppressLint("CheckResult")
    fun searchUserList(keyword: String) {
        if (keyword.isEmpty()) {
            mUserList.postValue(emptyList())
            return
        }

        ApiService.searchGeneralApi.searchUsers(mUserId, keyword)
            .map {
                it.getDataOrThrowable().users
            }
            .subscribeOn(Schedulers.io()).subscribe({
                mUserList.postValue(it)
            }, {
                mUserList.postValue(emptyList())
                mMessage.postValue(it.localizedMessage)
            })
    }

    @SuppressLint("CheckResult")
    fun addInvitedUser(user: User) {
        Single.fromCallable {
            return@fromCallable (mInvitedUserSet.value?.toMutableSet() ?: mutableSetOf()).apply {
                add(user)
            }.toSet()
        }.subscribeOn(Schedulers.io()).subscribe({
            mInvitedUserSet.postValue(it)
        }, {
            mMessage.postValue(it.localizedMessage)
        })
    }

    @SuppressLint("CheckResult")
    fun deleteInvitedUser(user: User) {
        Single.fromCallable {
            return@fromCallable (mInvitedUserSet.value?.toMutableSet() ?: mutableSetOf()).apply {
                remove(user)
            }.toSet()
        }.subscribeOn(Schedulers.io()).subscribe({
            mInvitedUserSet.postValue(it)
        }, {
            mMessage.postValue(it.localizedMessage)
        })
    }

    @SuppressLint("CheckResult")
    fun createChatRoom() {
        val userList = mInvitedUserSet.value ?: emptyList()

        if (userList.isEmpty()) {
            return
        }

        val userIdList = mutableListOf<String>().apply {
            addAll(userList.map { it.uid })
            add(mUserId)
        }

        val req = CreateRoomReq(
            roomReader = mUserId,
            companyName = "Smilegate Stove",
            userList = userIdList,
            roomName = TextUtils.join(",", userList.map { it.uname }),
            memberCount = userIdList.size
        )

        ApiService.roomApi.createRoom(req).map {
            it.getDataOrThrowable().room
        }.subscribeOn(Schedulers.io()).subscribe({
            mCreatedRoom.postValue(Pair(it, userIdList))
        }, {
            mMessage.postValue(it.localizedMessage)
        })
    }
}