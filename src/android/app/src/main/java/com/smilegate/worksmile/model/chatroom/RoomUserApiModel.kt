package com.smilegate.worksmile.model.chatroom

import com.google.gson.annotations.SerializedName
import com.smilegate.worksmile.model.User

data class UserResp(
    @SerializedName("users")
    val user: User
)

data class InviteRoomUsersReq(
    @SerializedName("ruuid")
    val roomId: String = "",
    @SerializedName("room_type")
    val room_type: String = "",
    @SerializedName("users")
    val users: List<String> = emptyList()
)

data class RoomUsers(
    @SerializedName("users")
    val users: List<User> = emptyList()
)