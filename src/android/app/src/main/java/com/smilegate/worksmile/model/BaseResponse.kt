package com.smilegate.worksmile.model

import com.google.gson.annotations.SerializedName

open class BaseResponse<T : Any>(
    @SerializedName("status")
    val status: Int = 200,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val data: T
) {
    @Throws
    fun getDataOrThrowable(): T {
        if (status == 200) {
            return data
        } else {
            throw Throwable(message)
        }
    }
}