package com.smilegate.worksmile.viewholder.chatroom

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.listener.ListItemClickListener
import com.smilegate.worksmile.adapter.listener.RoomItemClickListener
import com.smilegate.worksmile.common.CircleTransform
import com.smilegate.worksmile.model.chatroom.ChatRoom
import com.smilegate.worksmile.viewholder.BaseViewHolder
import com.squareup.picasso.Picasso

class ChatRoomViewHolder(view: View) : BaseViewHolder<ChatRoom>(view) {
    private val mPicasso = Picasso.get()

    private val mThumbnailList: List<ImageView> = listOf(
        view.findViewById(R.id.thumbnail_1),
        view.findViewById(R.id.thumbnail_2),
        view.findViewById(R.id.thumbnail_3),
        view.findViewById(R.id.thumbnail_4)
    )

    private val mRoomName: TextView = view.findViewById(R.id.tv_roomName_chatRoom)
    private val mLastMsg: TextView = view.findViewById(R.id.tv_lastMsg_chatRoom)
    private val mUnreadMsgCnt: TextView = view.findViewById(R.id.tv_unreadMsgCnt_chatRoom)
    private val mLastMsgTime: TextView = view.findViewById(R.id.tv_time_chatRoom)


    override fun bind(data: ChatRoom, itemClickListener: ListItemClickListener?) {
        mRoomName.text = data.roomName
        mLastMsg.text = data.lastMessage.orEmpty()

        if (data.unreadMessageCnt > 0) {
            mUnreadMsgCnt.visibility = View.VISIBLE
            mUnreadMsgCnt.text = data.unreadMessageCnt.toString()
        }

        itemView.setOnClickListener {
            (itemClickListener as? RoomItemClickListener)?.onRoomClicked(data)
        }

        itemView.setOnLongClickListener {
            (itemClickListener as? RoomItemClickListener)?.run {
                onRoomLongClicked(adapterPosition, data)
                true
            } ?: false
        }

        mLastMsgTime.text = data.lastMessageTime.orEmpty()

        mThumbnailList.forEach {
            it.visibility = View.GONE
        }

        data.thumbnails.take(4).forEachIndexed { index, thumbnailUrl ->
            mThumbnailList.getOrNull(index)?.let { thumbnailImage ->
                try {
                    mPicasso.load(thumbnailUrl).transform(CircleTransform()).into(thumbnailImage)
                    thumbnailImage.visibility = View.VISIBLE
                } catch (e: Exception){
                    thumbnailImage.visibility = View.GONE
                }
            }
        }
    }
}