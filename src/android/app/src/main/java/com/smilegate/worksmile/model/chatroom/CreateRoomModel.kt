package com.smilegate.worksmile.model.chatroom

import com.google.gson.annotations.SerializedName
import com.smilegate.worksmile.model.User

data class CreateRoomReq(
    @SerializedName("room_leader")
    val roomReader: String = "",
    @SerializedName("company_name")
    val companyName: String = "",
    @SerializedName("userlist")
    val userList: List<String> = emptyList(),
    @SerializedName("room_type")
    val roomType: String = "",
    @SerializedName("room_name")
    val roomName: String = "",
    @SerializedName("memcnt")
    val memberCount: Int = 0
)

class CreateRoomResp(
    @SerializedName("room")
    var room: ChatRoom,
    @SerializedName("users")
    var users: List<User>? = null
)

class DeleteRoomReqResp(
    @SerializedName("ruuid")
    var roomId: String
)

class RoomIdUserIdReq(
    @SerializedName("uid")
    val userId: String? = null,
    @SerializedName("ruuid")
    val roomId: String? = null
)