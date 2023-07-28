package com.itigradteamsix.snapshop.network

import kotlinx.coroutines.delay
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RateLimitInterceptor : Interceptor {
    private var lastRequestTime: Long = 0
    private val rateLimitTimeMillis: Long = 500 // Time between successive requests in milliseconds

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            val currentTime = System.currentTimeMillis()
            val timeSinceLastRequest = currentTime - lastRequestTime
            if (timeSinceLastRequest < rateLimitTimeMillis) {
                val delayMillis = rateLimitTimeMillis - timeSinceLastRequest
//                delay(delayMillis) // Use coroutine delay to introduce the delay
                Thread.sleep(delayMillis) // Use Thread.sleep to introduce the delay
            }
            lastRequestTime = System.currentTimeMillis()
        }
        return chain.proceed(chain.request())
    }
}
