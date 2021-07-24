package com.smilegate.worksmile.model.chatroom

import com.google.gson.annotations.SerializedName

class UploadFileResp(
    @SerializedName("file")
    val file: ChatFile
)

class ChatAttachmentResp(
    @SerializedName("collections")
    val collections: List<ChatFile> = emptyList(),
    @SerializedName("type")
    val type: String = ""
)