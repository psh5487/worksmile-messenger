package com.smilegate.worksmile.model.chat


import com.google.gson.annotations.SerializedName

data class SystemMessageContent(
    @SerializedName("last_read_idx")
    var lastReadIdx: Long?,
    @SerializedName("on_user_cnt")
    var onUserCnt: Int,
    @SerializedName("on_user_list")
    var onUserList: List<String>
)