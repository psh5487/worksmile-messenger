package com.smilegate.worksmile.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.chatroom.ChatRoomAdapter
import com.smilegate.worksmile.common.Constants
import com.smilegate.worksmile.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_simple_list.*

class ChatMemberActivity : BaseActivity() {
    private val mAdapter = ChatRoomAdapter()

    companion object {
        fun createIntent(context: Context, roomId: String): Intent {
            return Intent(context, ChatMemberActivity::class.java)
                .putExtra(Constants.EXTRA_ROOM_ID, roomId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_list)

        initViews()
        getRoomMembers(intent?.getStringExtra(Constants.EXTRA_ROOM_ID).orEmpty())
    }

    private fun initViews() {
        supportActionBar?.title = "대화상대 목록"

        recyclerView.run {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@ChatMemberActivity)
        }
    }

    @SuppressLint("CheckResult")
    private fun getRoomMembers(roomId: String) {
        if (roomId.isBlank()) {
            finish()
            return
        }

        ApiService.roomApi.getMemberList(roomId)
            .map { it.getDataOrThrowable().users }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mAdapter.setItems(it)
            }, {
            })
    }
}