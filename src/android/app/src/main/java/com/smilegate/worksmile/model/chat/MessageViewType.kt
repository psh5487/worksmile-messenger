package com.smilegate.worksmile.model.chat

enum class MessageViewType(val viewType: Int) {
    EMPTY(0), SENT(1), RECEIVED(2), DATE_DIVIDER(3);

    companion object {
        fun get(viewType: Int): MessageViewType {
            return values().firstOrNull { it.viewType == viewType } ?: EMPTY
        }
    }
}