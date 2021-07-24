package com.smilegate.worksmile.network

import com.smilegate.worksmile.model.BaseResponse
import com.smilegate.worksmile.model.auth.Company
import io.reactivex.Single
import retrofit2.http.GET

interface UserApi {
    @GET("/api/user/companylist/subroot")
    fun getCompanyList(): Single<BaseResponse<List<Company>>>
}