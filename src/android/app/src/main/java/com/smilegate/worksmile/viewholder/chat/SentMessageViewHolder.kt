package com.smilegate.worksmile.viewholder.chat

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.listener.MessageClickListener
import com.smilegate.worksmile.extensions.setVisibility
import com.smilegate.worksmile.model.chat.MessageType
import com.smilegate.worksmile.model.chat.ChatMessage

class SentMessageViewHolder(view: View) : MessageViewHolder<ChatMessage>(view) {
    private val mMessage: TextView = view.findViewById(R.id.tv_sentMsg)
    private val mThumbnail: ImageView = view.findViewById(R.id.iv_thumbnail)
    private val mMessageTime: TextView = view.findViewById(R.id.tv_time_sentMsg)
    private val mUnreadCnt: TextView = view.findViewById(R.id.tv_unreadMsgCnt_sentMsg)
    private val mMessageTitle: TextView = view.findViewById(R.id.tv_title_sendMsg)
    private val mMessageLayout: ViewGroup = view.findViewById(R.id.cv_sentMsg)
    private val mTitleLayout: ViewGroup = view.findViewById(R.id.title_layout)
    private val mTitleImage: ImageView = view.findViewById(R.id.title_image)

    @SuppressLint("SimpleDateFormat")
    override fun bind(data: ChatMessage, itemClickListener: MessageClickListener?) {
        mMessageLayout.setOnLongClickListener {
            itemClickListener?.onMessageLongClicked(data)
            true
        }

        when (data.type) {
            MessageType.NOTIFY -> {
                mMessageTitle.run {
                    text = "공지로 등록됨"
                    setVisibility(true)
                }
                mThumbnail.setVisibility(false)
                mTitleLayout.setVisibility(true)
            }
            MessageType.REPLY -> {
                data.parentMessageContent?.let {
                    if (setImage(it, mTitleImage)) {
                        mMessageTitle.setVisibility(false)
                    } else {
                        mMessageTitle.run {
                            setVisibility(true)
                            text = it
                        }
                    }
                }
                mTitleLayout.setVisibility(!data.parentMessageContent.isNullOrBlank())
            }
            else -> {
                mTitleLayout.setVisibility(false)
            }
        }

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

        data.content.toString().let { message ->
            when {
                setImage(message, mThumbnail) -> {
                    mMessage.setVisibility(false)

                    itemView.setOnClickListener {
                        itemClickListener?.onImageClicked(message)
                    }
                }
                else -> {
                    mThumbnail.setVisibility(false)

                    mMessage.run {
                        setMessage(mMessage, message, data.keyword.orEmpty())
                        setVisibility(true)
                    }
                }
            }
        }
    }
}