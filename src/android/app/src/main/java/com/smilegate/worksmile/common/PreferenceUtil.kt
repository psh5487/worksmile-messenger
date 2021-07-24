package com.smilegate.worksmile.common

import android.content.Context
import android.content.SharedPreferences

object PreferenceUtil {
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)
    }

    val userId: String
        get() = prefs?.getString(Constants.KEY_USER_ID, "") ?: ""

    val userPassword: String
        get() = prefs?.getString(Constants.KEY_USER_PW, "") ?: ""

    val userToken: String
        get() = prefs?.getString(Constants.KEY_USER_TOKEN, "") ?: ""

    fun getString(key: String, defValue: String): String {
        return prefs?.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs?.run {
            edit().putString(key, str).apply()
        }
    }
}