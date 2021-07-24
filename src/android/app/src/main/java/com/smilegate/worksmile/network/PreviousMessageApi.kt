package com.smilegate.worksmile.network

import com.smilegate.worksmile.model.BaseResponse
import com.smilegate.worksmile.model.chat.ChatMessage
import com.smilegate.worksmile.model.chat.MessageList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PreviousMessageApi {
    @GET("/api/messages/msg/room/{roomId}")
    fun getPreviousMessageList(
        @Path("roomId") roomId: String,
        @Query("start") start: Long,
        @Query("num") num: Int
    ): Single<BaseResponse<MessageList>>
}