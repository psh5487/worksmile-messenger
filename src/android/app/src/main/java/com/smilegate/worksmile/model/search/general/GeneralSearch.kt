package com.smilegate.worksmile.model.search.general

import com.google.gson.annotations.SerializedName
import com.smilegate.worksmile.model.User
import com.smilegate.worksmile.model.chatroom.ChatRoom

data class GeneralSearch(
    @SerializedName("rooms")
    var rooms: List<ChatRoom>,
    @SerializedName("users")
    var users: List<User>
)
