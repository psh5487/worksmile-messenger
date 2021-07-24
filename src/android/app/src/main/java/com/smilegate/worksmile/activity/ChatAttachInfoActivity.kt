package com.smilegate.worksmile.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.chatroom.AttachmentAdapter
import com.smilegate.worksmile.common.Constants
import com.smilegate.worksmile.model.chat.AttachmentType
import com.smilegate.worksmile.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_simple_list.*
import kotlinx.android.synthetic.main.activity_text_collect.*

class ChatAttachInfoActivity : BaseActivity() {
    private val mAttachmentAdapter = AttachmentAdapter()
    private var mRoomId: String = ""
    private var mAttachType: AttachmentType = AttachmentType.IMAGE

    companion object {
        fun createIntent(context: Context, roomId: String, type: AttachmentType): Intent {
            return Intent(context, ChatAttachInfoActivity::class.java)
                .putExtra(Constants.EXTRA_ROOM_ID, roomId)
                .putExtra(Constants.EXTRA_ATTACHMENT_TYPE, type)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_list)

        intent?.run {
            mRoomId = getStringExtra(Constants.EXTRA_ROOM_ID).orEmpty()
            mAttachType = (getSerializableExtra(Constants.EXTRA_ATTACHMENT_TYPE) as? AttachmentType)
                ?: AttachmentType.IMAGE
        }

        if (mRoomId.isBlank()) {
            finish()
            return
        }

        initView()
        getAttachment()
    }

    private fun initView() {
        supportActionBar?.title = when (mAttachType) {
            AttachmentType.IMAGE -> "첨부 이미지 목록"
            AttachmentType.FILE -> "첨부 파일 목록"
            AttachmentType.LINK -> "첨부 링크 목록"
            AttachmentType.NOTIFY -> "공지 목록"
        }

        recyclerView.run {
            adapter = mAttachmentAdapter
            layoutManager = if (mAttachType == AttachmentType.IMAGE) GridLayoutManager(
                this@ChatAttachInfoActivity,
                3
            ) else LinearLayoutManager(this@ChatAttachInfoActivity)
        }
    }

    @SuppressLint("CheckResult")
    private fun getAttachment() {
        ApiService.roomApi.getCollectionList(mRoomId, mAttachType.type)
            .map { it.getDataOrThrowable().collections }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mAttachmentAdapter.setItems(it)
            }, {})
    }
}