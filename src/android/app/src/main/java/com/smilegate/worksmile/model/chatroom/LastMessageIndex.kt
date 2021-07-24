package com.smilegate.worksmile.model.chatroom

import com.google.gson.annotations.SerializedName

data class LastMessageIndex(
    @SerializedName("uid")
    val userId: String = "",
    @SerializedName("ruuid")
    val roomId: String = "",
    @SerializedName("room_last_message_idx")
    val lastMessageIndex: Long = 0
)
