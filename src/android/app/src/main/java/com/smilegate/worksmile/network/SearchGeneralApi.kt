package com.smilegate.worksmile.network

import com.smilegate.worksmile.model.BaseResponse
import com.smilegate.worksmile.model.Users
import com.smilegate.worksmile.model.search.general.GeneralSearch
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchGeneralApi {
    @GET("/api/chat/search/cur-user/{userId}")
    fun getSearchResults(
        @Path("userId") userId: String,
        @Query("keyword") keyword: String
    ): Single<BaseResponse<GeneralSearch>>

    @GET("/api/chat/search/user/cur-user/{userId}")
    fun searchUsers(
        @Path("userId") userId: String,
        @Query("keyword") keyword: String
    ): Single<BaseResponse<Users>>
}