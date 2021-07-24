package com.smilegate.worksmile.model.chat

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.smilegate.worksmile.common.PreferenceUtil
import com.smilegate.worksmile.model.chatroom.ChatRoomItem
import java.text.SimpleDateFormat
import java.util.*

open class ChatMessage(
    @SerializedName("midx")
    var midx: Long? = null,
    @SerializedName("ruuid")
    var ruuid: String = "",
    @SerializedName("sender")
    var sender: String = "",
    @SerializedName("type")
    var type: MessageType? = null,
    @SerializedName("content")
    var content: Any,
    @SerializedName("device")
    var device: String? = null,
    @SerializedName("parent_id")
    var parent_id: Long? = null,
    @SerializedName("uname")
    var uname: String? = null,
    @SerializedName("cname")
    var cname: String? = null, // 그룹명
    @SerializedName("pname")
    var pname: String? = null, // 직급명
    @SerializedName("created_at")
    var created_at: String? = null,
    @SerializedName("deleted_at")
    var deleted_at: String? = null
) : ChatRoomItem {
    var userThumbnail: String? = null

    var parentMessageContent: String? = null

    var keyword: String? = null

    var unReadCount: Int? = null

    val isValidMessage: Boolean
        get() = type in MessageType.validMessageTypes

    val time: String
        get() = date?.let {
            try {
                SimpleDateFormat("a hh:mm").format(it)
            } catch (e: Exception) {
                ""
            }
        } ?: ""

    val date: Date?
        @SuppressLint("SimpleDateFormat")
        get() = try {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(created_at.orEmpty())
        } catch (e: Exception) {
            null
        }

    override fun getViewType(): MessageViewType {
        return if (sender == PreferenceUtil.userId) MessageViewType.SENT else MessageViewType.RECEIVED
    }
}