package com.itigradteamsix.snapshop.settings.currency

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object CurrencyApiClient {

    fun getApiService(): CurrencyApiService {

        val retrofit = Retrofit.Builder()
            .baseUrl(CurrencyEndPoints.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(CurrencyApiService::class.java)

    }
}