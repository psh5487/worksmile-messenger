package com.smilegate.worksmile.model.chatroom

import com.google.gson.annotations.SerializedName

data class ChatFile(
    @SerializedName("file_name")
    val fileName: String = "",
    @SerializedName("s3_url")
    val fileUrl: String = "",
    @SerializedName("type")
    val type: String = ""
)
