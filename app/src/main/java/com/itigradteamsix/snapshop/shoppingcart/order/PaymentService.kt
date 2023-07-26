package com.itigradteamsix.snapshop.shoppingcart.order

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PaymentService {
    @FormUrlEncoded
    @POST("/create-payment-intent/")
    fun createPaymentIntent(
        @Field("amount") amount: Double,
        @Field("currency") currency: String
    ): Call<PaymentIntentResponse>

}