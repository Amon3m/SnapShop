package com.itigradteamsix.snapshop

import android.app.Application
import android.content.Context
import com.itigradteamsix.snapshop.settings.data.SettingsStore

class MyApplication : Application() {
    lateinit var settingsStore: SettingsStore

    companion object {
        lateinit var appInstance: MyApplication
            private set
        lateinit var appContext: Context
            private set
    }
    override fun onCreate() {
        super.onCreate()
        settingsStore = SettingsStore(this)
        appContext = applicationContext
        appInstance = this

    }
}