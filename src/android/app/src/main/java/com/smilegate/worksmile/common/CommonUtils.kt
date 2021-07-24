package com.smilegate.worksmile.common

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat

object CommonUtils {
    fun requestPermission(
        activity: Activity,
        permissions: Array<String>,
        requestCode: Int,
        postAction: () -> Unit
    ) {
        val notGrantedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (notGrantedPermissions.isNotEmpty()) {
            requestPermissions(
                activity,
                notGrantedPermissions,
                requestCode
            )
        } else {
            postAction.invoke()
        }
    }
}