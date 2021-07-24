package com.smilegate.worksmile.common

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.core.FlipperClient
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.navigation.NavigationFlipperPlugin
import com.facebook.soloader.SoLoader
import com.smilegate.worksmile.BuildConfig
import com.smilegate.worksmile.network.ApiService

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        PreferenceUtil.init(this)

        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client: FlipperClient = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.start()
        }

        AndroidFlipperClient.getInstance(this).apply {
            addPlugin(
                InspectorFlipperPlugin(
                    this@MyApplication,
                    DescriptorMapping.withDefaults()
                )
            )
            addPlugin(NavigationFlipperPlugin.getInstance())
            addPlugin(ApiService.networkFlipperPlugin)
        }.start()
    }
}