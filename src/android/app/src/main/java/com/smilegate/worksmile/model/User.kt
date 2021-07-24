package com.smilegate.worksmile.model


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("all_push_notice")
    var allPushNotice: String,
    @SerializedName("cid")
    var cid: Int,
    @SerializedName("email")
    var email: String,
    @SerializedName("login_at")
    var loginAt: Any,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("pid")
    var pid: Int,
    @SerializedName("profile")
    var profile: String,
    @SerializedName("pwd")
    var pwd: String,
    @SerializedName("register_at")
    var registerAt: String,
    @SerializedName("role")
    var role: String,
    @SerializedName("root_cid")
    var rootCid: Int,
    @SerializedName("salt")
    var salt: String,
    @SerializedName("subroot_cid")
    var subRootCid: Int,
    @SerializedName("uid")
    var uid: String,
    @SerializedName("uname")
    var uname: String,
    @SerializedName("uuid")
    var uuid: String? = null,
    @SerializedName("last_read_idx")
    var lastReadIdx: Long? = null
) : BaseItem() {
    override fun getViewType(): Int {
        return BaseItemViewType.USER.viewType
    }
}

data class UserId(
    @SerializedName("uid")
    val uid: String
)

data class IsUniqueRes(
    @SerializedName("uid")
    val uid: String,
    @SerializedName("is_unique")
    val is_unique: String
)

data class Users(
    @SerializedName("users")
    val users: List<User> = emptyList()
)

data class UserIdList(
    @SerializedName("users")
    val users: List<String> = emptyList()
)