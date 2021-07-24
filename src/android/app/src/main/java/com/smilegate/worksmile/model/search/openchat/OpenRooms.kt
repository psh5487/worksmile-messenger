package com.smilegate.worksmile.model.search.openchat


import com.google.gson.annotations.SerializedName

data class OpenRooms(
    @SerializedName("open_rooms")
    var openRooms: List<OpenRoom>
)