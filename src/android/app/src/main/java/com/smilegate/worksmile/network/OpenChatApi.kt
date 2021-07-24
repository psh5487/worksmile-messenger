package com.smilegate.worksmile.network

import com.smilegate.worksmile.model.BaseResponse
import com.smilegate.worksmile.model.search.openchat.OpenRooms
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenChatApi {
    @GET("/api/chat/search/public-room/cur-user/{userId}")
    fun getChannelList(
        @Path("userId") userId: String,
        @Query("rname") rname: String
    ): Single<BaseResponse<OpenRooms>>
}