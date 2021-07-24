package com.smilegate.worksmile.network

import com.smilegate.worksmile.model.BaseResponse
import com.smilegate.worksmile.model.UserId
import com.smilegate.worksmile.model.auth.LoginRequest
import com.smilegate.worksmile.model.auth.LoginResult
import com.smilegate.worksmile.model.auth.LogoutRequest
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApi {
    @POST("/api/auth/login")
    fun getLoginRes(@Body req: LoginRequest): Single<BaseResponse<LoginResult>>

    @POST("/api/auth/logout")
    fun getLogoutReq(
        @Header("X-Auth-Token") token: String,
        @Body req: LogoutRequest
    ): Single<BaseResponse<UserId>>
}