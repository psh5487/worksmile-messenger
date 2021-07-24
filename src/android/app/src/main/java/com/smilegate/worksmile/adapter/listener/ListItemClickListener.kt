package com.smilegate.worksmile.adapter.listener

import com.smilegate.worksmile.model.User
import com.smilegate.worksmile.model.chat.ChatMessage
import com.smilegate.worksmile.model.chatroom.ChatRoom

interface ListItemClickListener

interface RoomItemClickListener : ListItemClickListener {
    fun onRoomClicked(item: ChatRoom)
    fun onRoomLongClicked(position: Int, item: ChatRoom)
}

interface UserClickListener : ListItemClickListener {
    fun onUserClicked(user: User)
}

interface SearchGeneralItemClickListener : RoomItemClickListener, UserClickListener

interface MessageClickListener : ListItemClickListener {
    fun onMessageLongClicked(message: ChatMessage)
    fun onImageClicked(imageUrl: String)
}