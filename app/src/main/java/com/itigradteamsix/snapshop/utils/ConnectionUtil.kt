package com.itigradteamsix.snapshop.utils

import android.content.Context
import android.net.ConnectivityManager
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

object ConnectionUtil {

//    @OptIn(ExperimentalCoroutinesApi::class)
//   fun performNetworkCheck(flow: Flow<ApiState>?, context: Context): Flow<out T>? =
//        flow?.flatMapLatest {
//            if (isNetworkAvailable(context) && it is ApiState.Success<*>) {
//                flowOf(ApiState.Success(it))
//            } else {
//                flowOf(ApiState.Failure("No internet connection"))
//            }
//        }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun <T> performNetworkCheck(flow: Flow<T>?, context: Context): Flow<ApiState>? =
        flow?.let {
//            if (isNetworkAvailable(context)) {
            flowOf(ApiState.Success(it))
//            } else {
//                flowOf(ApiState.Failure("No internet connection"))
//            }
        }
            ?: flowOf(ApiState.Failure("No internet connection"))


    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}