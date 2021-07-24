package com.smilegate.worksmile.network

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.smilegate.worksmile.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    val networkFlipperPlugin by lazy {
        NetworkFlipperPlugin()
    }

    private fun getRetrofit(baseUrl: String): Retrofit {
        val client = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addNetworkInterceptor(FlipperOkhttpInterceptor(networkFlipperPlugin))
                addInterceptor(FlipperOkhttpInterceptor(networkFlipperPlugin, true))
            }
        }.build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    val searchOpenChatApi: OpenChatApi by lazy {
        getRetrofit(BuildConfig.CHAT_SERVER_URL).create(OpenChatApi::class.java)
    }

    val roomApi: RoomApi by lazy {
        getRetrofit(BuildConfig.CHAT_SERVER_URL).create(RoomApi::class.java)
    }

    val searchGeneralApi: SearchGeneralApi by lazy {
        getRetrofit(BuildConfig.CHAT_SERVER_URL).create(SearchGeneralApi::class.java)
    }

    val loginApi: LoginApi by lazy {
        getRetrofit(BuildConfig.MESSAGE_SERVER_URL).create(LoginApi::class.java)
    }

    val signUpApi: SignUpApi by lazy {
        getRetrofit(BuildConfig.MESSAGE_SERVER_URL).create(SignUpApi::class.java)
    }

    val previousMessageApi: PreviousMessageApi by lazy {
        getRetrofit(BuildConfig.CHAT_SERVER_URL).create(PreviousMessageApi::class.java)
    }

    val userApi: UserApi by lazy {
        getRetrofit(BuildConfig.USER_SERVER_URL).create(UserApi::class.java)
    }

}