package com.smilegate.worksmile.model.search.openchat


import com.google.gson.annotations.SerializedName

data class OpenRoom(
    @SerializedName("init_name")
    var initName: String,
    @SerializedName("leader_id")
    var leaderId: String,
    @SerializedName("memcnt")
    var memcnt: Int,
    @SerializedName("register_at")
    var registerAt: Long,
    @SerializedName("room_type")
    var roomType: String,
    @SerializedName("root_cid")
    var rootCid: Int,
    @SerializedName("ruuid")
    var ruuid: String,
    @SerializedName("update_at")
    var updateAt: Long
)