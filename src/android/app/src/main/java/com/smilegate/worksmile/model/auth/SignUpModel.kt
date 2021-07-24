package com.smilegate.worksmile.model.auth


import com.google.gson.annotations.SerializedName

data class SignUpResult(
    @SerializedName("cname")
    var cname: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("uid")
    var uid: String,
    @SerializedName("uname")
    var uname: String
)

data class SignUpRequest(
    @SerializedName("subroot_cid")
    var subroot_cid: Int,
    @SerializedName("subroot_cname")
    var subroot_cname: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("pwd")
    var pwd: String,
    @SerializedName("type")
    var type: String,
    @SerializedName("uid")
    var uid: String,
    @SerializedName("uname")
    var uname: String
)

data class UniqueCheckRequest(
    @SerializedName("uid")
    var uid: String
)

data class Company(
    @SerializedName("cid")
    var cid: Int,
    @SerializedName("cname")
    var cname: String,
    @SerializedName("isSubroot")
    var isSubroot: String
)