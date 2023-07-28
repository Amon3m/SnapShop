package com.itigradteamsix.snapshop

import android.app.Application
import android.content.Context
import com.itigradteamsix.snapshop.settings.data.SettingsStore
import com.stripe.android.PaymentConfiguration

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

        //Stripe publishable key so that it can make requests to the Stripe API
        PaymentConfiguration.init(appContext,"pk_test_51NVjXWGgec2y6RBTEnPK2fhQBQat9EHhrF78p2qKuNRRmhwQXZy6IoU3Ir9jWZBfLz1fwwcJfRVKKjvTXcEMVvn400yhB7jfi5")

    }
}