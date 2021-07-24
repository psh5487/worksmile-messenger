package com.smilegate.worksmile.adapter.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.listener.MessageClickListener
import com.smilegate.worksmile.model.chat.ChatMessage
import com.smilegate.worksmile.model.chat.MessageViewType
import com.smilegate.worksmile.model.chatroom.ChatRoomItem
import com.smilegate.worksmile.viewholder.chat.MessageViewHolder
import com.smilegate.worksmile.viewholder.chat.ReceivedMessageViewHolder
import com.smilegate.worksmile.viewholder.chat.SentMessageViewHolder
import com.smilegate.worksmile.viewholder.chatroom.DateDividerViewHolder

class MessageAdapter : RecyclerView.Adapter<MessageViewHolder<ChatRoomItem>>() {

    private val mMessageList: MutableList<ChatRoomItem> = mutableListOf()

    var itemClickListener: MessageClickListener? = null

    val lastMessageIndex: Long
        get() = synchronized(mMessageList) {
            return@synchronized (mMessageList.lastOrNull { it is ChatMessage && it.isValidMessage } as? ChatMessage)?.midx
                ?: 0
        }

    val messageList: List<ChatRoomItem> = mMessageList

    override fun getItemViewType(position: Int): Int {
        return mMessageList[position].getViewType().viewType
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder<ChatRoomItem> {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            MessageViewType.SENT.viewType -> {
                SentMessageViewHolder(
                    layoutInflater.inflate(
                        R.layout.item_sent_message,
                        parent,
                        false
                    )
                )
            }
            MessageViewType.RECEIVED.viewType -> {
                ReceivedMessageViewHolder(
                    layoutInflater.inflate(
                        R.layout.item_received_message,
                        parent,
                        false
                    )
                )
            }
            else -> {
                DateDividerViewHolder(
                    layoutInflater.inflate(
                        R.layout.item_date_divider,
                        parent,
                        false
                    )
                )
            }
        } as MessageViewHolder<ChatRoomItem>
    }

    fun setChatMessageList(messages: List<ChatRoomItem>) {
        synchronized(mMessageList) {
            mMessageList.run {
                clear()
                addAll(messages)
            }
        }

        notifyDataSetChanged()
    }

    fun setChatMessage(position: Int, message: ChatMessage) {
        synchronized(mMessageList) {
            mMessageList[position] = message
        }

        notifyItemChanged(position)
    }

    fun addChatMessage(message: ChatMessage) {
        synchronized(mMessageList) {
            val lastIndex = mMessageList.lastIndex + 1
            mMessageList.add(lastIndex, message)
            notifyItemInserted(lastIndex)
        }
    }

    override fun getItemCount(): Int {
        return mMessageList.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder<ChatRoomItem>, position: Int) {
        holder.bind(mMessageList[position], itemClickListener)
    }
}