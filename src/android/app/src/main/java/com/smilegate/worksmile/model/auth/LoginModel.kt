package com.smilegate.worksmile.model.auth

import com.google.gson.annotations.SerializedName
import com.smilegate.worksmile.model.User

data class LoginRequest(
    @SerializedName("uid")
    var uid: String,
    @SerializedName("pwd")
    var pwd: String?
)

data class LogoutRequest(
    @SerializedName("uid")
    var uid: String
)

data class LoginResult(
    @SerializedName("X-Auth-Token")
    val token: String = "",
    @SerializedName("user")
    val user: User,
)