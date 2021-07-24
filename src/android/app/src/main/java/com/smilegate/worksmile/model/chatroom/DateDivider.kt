package com.smilegate.worksmile.model.chatroom

import com.smilegate.worksmile.model.chat.MessageViewType

data class DateDivider(
    val date: String = ""
) : ChatRoomItem {
    override fun getViewType(): MessageViewType {
        return MessageViewType.DATE_DIVIDER
    }
}
