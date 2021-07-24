package com.smilegate.worksmile.model.chatroom

import com.google.gson.annotations.SerializedName
import com.smilegate.worksmile.model.BaseItem
import com.smilegate.worksmile.model.BaseItemViewType
import com.smilegate.worksmile.model.chat.ChatMessage

data class RoomList(
    @SerializedName("image_list")
    var imageList: List<String> = emptyList(),
    @SerializedName("last_messages")
    var lastMessages: List<LastMessage> = emptyList(),
    @SerializedName("rooms")
    var rooms: List<ChatRoom> = emptyList()
)


data class ChatRoom(
    @SerializedName("favorite_type")
    var favoriteType: String = "off",
    @SerializedName("last_read_idx")
    var lastReadIdx: Int,
    @SerializedName("push_notice")
    var pushNotice: String,
    @SerializedName("rname")
    var roomName: String,
    @SerializedName("ruuid")
    var roomId: String,
    @SerializedName("uuid")
    var uuid: String,
    @SerializedName("memcnt")
    var memcnt: Int,
    @SerializedName("register_at")
    var register_at: String,
    @SerializedName("update_at")
    var update_at: String,
    @SerializedName("room_type")
    var room_type: String,
    @SerializedName("leader_id")
    var leader_id: String,
    @SerializedName("leader_name")
    var leader_name: String,
    @SerializedName("init_name")
    var init_name: String,
    @SerializedName("thumbnails")
    var thumbnails: List<String> = emptyList()
) : BaseItem() {
    var lastMessage: String? = null
    var lastMessageTime: String? = null
    var unreadMessageCnt: Long = 0

    val isFavorite: Boolean
        get() = favoriteType == "on"

    override fun getViewType(): Int {
        return BaseItemViewType.ROOM.viewType
    }
}

data class LastMessage(
    @SerializedName("_id")
    var id: String,
    @SerializedName("msg")
    var massageList: List<ChatMessage>,
    @SerializedName("ruuid")
    var ruuid: String
)