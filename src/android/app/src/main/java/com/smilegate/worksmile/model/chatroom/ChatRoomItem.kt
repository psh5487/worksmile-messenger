package com.smilegate.worksmile.model.chatroom

import com.smilegate.worksmile.model.chat.MessageViewType

interface ChatRoomItem {
    fun getViewType(): MessageViewType
}