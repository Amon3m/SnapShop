package com.itigradteamsix.snapshop

import android.content.Context
import android.net.ConnectivityManager

object Utilities {
    const val WOMEN_COLLECTION_ID=453400527149
    const val MEN_COLLECTION_ID=453400494381
    const val KIDS_COLLECTION_ID=453400559917
    const val SALE_COLLECTION_ID=453400592685

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
