package com.smilegate.worksmile.network

import com.smilegate.worksmile.model.*
import com.smilegate.worksmile.model.auth.SignUpRequest
import com.smilegate.worksmile.model.auth.SignUpResult
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {
    @POST("/api/auth/join")
    fun getSignUpRes(@Body req: SignUpRequest): Single<BaseResponse<SignUpResult>>

    @POST("/api/auth/id/unique")
    fun isUniqueReq(@Body req: UserId): Single<BaseResponse<IsUniqueRes>>
}