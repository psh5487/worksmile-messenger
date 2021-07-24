package com.smilegate.worksmile.viewholder.chat

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.listener.MessageClickListener
import com.smilegate.worksmile.extensions.setVisibility
import com.smilegate.worksmile.model.chat.ChatMessage

class ReceivedMessageViewHolder(view: View) : MessageViewHolder<ChatMessage>(view) {
    private val mMemberThumbnail: ImageView = view.findViewById(R.id.iv_member_thumbnail_msgRec)
    private val mName: TextView = view.findViewById(R.id.tv_name_msgRec)
    private val mMessage: TextView = view.findViewById(R.id.tv_content_msgRec)
    private val mMessageThumbnail: ImageView = view.findViewById(R.id.iv_thumbnail_msgRec)
    private val mMessageTime: TextView = view.findViewById(R.id.tv_time_msgRec)
    private val mUnreadCnt: TextView = view.findViewById(R.id.tv_unreadMsgCnt_msgRec)
    private val mMessageLayout: ViewGroup = view.findViewById(R.id.cv_msgRec)

    @SuppressLint("SetTextI18n")
    override fun bind(data: ChatMessage, itemClickListener: MessageClickListener?) {
        mName.text = StringBuilder().apply {
            append(data.uname ?: "알 수 없는 사용자")

            data.cname.takeIf { !it.isNullOrBlank() }?.let {
                append(" / ")
                append(it)
            }

            data.pname.takeIf { !it.isNullOrBlank() }?.let {
                append(" / ")
                append(it)
            }
        }.toString()

        mMessageTime.text = data.time

        mUnreadCnt.run {
            data.unReadCount?.let {
                if (it == 0) {
                    setVisibility(false)
                } else {
                    text = it.toString()
                    setVisibility(true)
                }
            } ?: {
                setVisibility(false)
            }
        }

        mPicasso.load(data.userThumbnail.orEmpty()).error(R.drawable.user_img)
            .into(mMemberThumbnail)

        data.content.toString().let { message ->
            when {
                setImage(message, mMessageThumbnail) -> {
                    mMessage.setVisibility(false)

                    itemView.setOnClickListener {
                        itemClickListener?.onImageClicked(message)
                    }
                }
                else -> {
                    mMessageThumbnail.setVisibility(false)

                    mMessage.run {
                        setMessage(mMessage, message, data.keyword.orEmpty())
                        setVisibility(true)
                    }
                }
            }
        }

        mMessageLayout.setOnLongClickListener {
            itemClickListener?.onMessageLongClicked(data)
            true
        }
    }
}