package com.itigradteamsix.snapshop.data.repository.remote

import com.itigradteamsix.snapshop.data.models.Customer
import com.itigradteamsix.snapshop.data.models.CustomerResponse
import com.itigradteamsix.snapshop.data.models.Product
import com.itigradteamsix.snapshop.data.models.SmartCollection
import com.itigradteamsix.snapshop.data.models.SmartCollectionResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ShopifyApiClient {
    private const val BASE_URL = "https://itp-sv-and6.myshopify.com/admin/api/2023-07/"
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
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

    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }



    suspend fun getAllProducts(): List<Product>? {
        var response: List<Product>? = null
        try {
            val wholeResponse = apiService.getAllProducts()
            response =  wholeResponse.products
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }


    suspend fun getProductsByCollectionId(collectionId: Long): List<Product>? {
        var response: List<Product>? = null
        try {
            val wholeResponse = apiService.getProductsByCollectionId(collectionId)
            response =  wholeResponse.products
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }


    suspend fun getANumberOfProducts(number: Int): List<Product>? {
        var response: List<Product>? = null
        try {
            val wholeResponse = apiService.getANumberOfProducts(number)
            response =  wholeResponse.products
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }


    suspend fun getSmartCollectionById(id: Long): SmartCollection? {
        var collection: SmartCollection? = null
        try {
            val response = apiService.getSmartCollectionById(id)
            collection =  response.smart_collection
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return collection
    }

    suspend fun getSmartCollections(): List<SmartCollection>? {
        var response: List<SmartCollection>? = null
        try {
            val wholeResponse = apiService.getSmartCollections()
            response =  wholeResponse.smart_collections
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun createCustomer(customer: Customer): Customer? {
        var response: Customer? = null
        try {
            val wholeResponse = apiService.createCustomer(customer)
            response =  wholeResponse.customer
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    suspend fun getCustomerByEmail(email: String): Customer? {
        var response: Customer? = null
        try {
            val wholeResponse = apiService.getCustomerByEmail(email)
            response =  wholeResponse.customer
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }


}