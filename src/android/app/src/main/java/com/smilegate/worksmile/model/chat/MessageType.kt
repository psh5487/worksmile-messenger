package com.smilegate.worksmile.model.chat

enum class MessageType {
    ENTER, EXIT, ON, OFF, TALK, IMAGE, NOTIFY, REPLY;

    companion object {
        val validMessageTypes = arrayOf(TALK, IMAGE, NOTIFY, REPLY)
    }
}