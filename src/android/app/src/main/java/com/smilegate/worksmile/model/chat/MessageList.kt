package com.smilegate.worksmile.model.chat


import com.google.gson.annotations.SerializedName

data class MessageList(
    @SerializedName("messages")
    var messages: List<ChatMessage>
)