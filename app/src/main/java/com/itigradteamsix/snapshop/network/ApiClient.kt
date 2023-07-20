package com.itigradteamsix.snapshop.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.data.models.Customer
import com.itigradteamsix.snapshop.data.repository.remote.ApiServices

import com.itigradteamsix.snapshop.model.ProductListResponse
import com.itigradteamsix.snapshop.model.SmartCollectionResponse
import com.itigradteamsix.snapshop.model.SmartCollectionsResponse
import com.itigradteamsix.snapshop.network.Api.apiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://itp-sv-and6.myshopify.com/admin/api/2023-07/"

object ApiClient : RemoteSource {

    override suspend fun getAllProducts(): ProductListResponse {


        return Api.apiService.getAllProducts()
    }

    override suspend fun getProductsByCollectionId(collectionId: Long): ProductListResponse {
        return Api.apiService.getProductsByCollectionId(collectionId)


    }

    override suspend fun getANumberOfProducts(number: Int): ProductListResponse {

        return Api.apiService.getANumberOfProducts(number)

    }

    override suspend fun getSmartCollectionById(id: Long): SmartCollectionResponse {

        return Api.apiService.getSmartCollectionById(id)

    }

    override suspend fun getSmartCollections(): SmartCollectionsResponse{

        return Api.apiService.getSmartCollections()

     }
    override suspend fun createCustomer(customer: CustomerResponse): Customer? {
        var response: Customer? = null
        try {
            val wholeResponse = apiService.createCustomer(customer)
            response =  wholeResponse.customers
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfCreateCustException",e.message.toString())

        }
        Log.d("retrofitCreateCust", response?.email.toString())
        return response
    }
    override suspend fun getCustomerByEmail(email: String): List<Customer>? {
        var response: List<Customer>? = null
        try {
            val wholeResponse = apiService.getCustomerByEmail(email)
            Log.d("retrofitCreateCustres", wholeResponse.toString())
            response =  wholeResponse.customers
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfGetCustException",e.message.toString())

        }
        Log.d("retrofitCreateCust", response?.get(0)?.email.toString())
        return response
    }


}

object RetrofitHelper {
    var gson: Gson = GsonBuilder().create()
    var retrofitInstance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)

        .build()
}
private val okHttpClient by lazy {
    OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-Shopify-Access-Token", "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")
                .build()
            chain.proceed(request)
        }
        .build()
}

object Api {
    val apiService: ApiServices by lazy {
        RetrofitHelper.retrofitInstance.create(ApiServices::class.java)
    }
}