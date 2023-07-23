package com.itigradteamsix.snapshop.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.data.repository.remote.ApiServices
import com.itigradteamsix.snapshop.model.Customer
import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.model.DraftOrderRequest
import com.itigradteamsix.snapshop.model.ListProductsResponse

import com.itigradteamsix.snapshop.model.Product
import com.itigradteamsix.snapshop.model.ProductListResponse
import com.itigradteamsix.snapshop.model.SmartCollectionResponse
import com.itigradteamsix.snapshop.model.SmartCollectionsResponse
import com.itigradteamsix.snapshop.network.Api.apiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



const val BASE_URL = "https://itp-sv-and6.myshopify.com/admin/api/2023-07/"

object ApiClient : RemoteSource {

    override suspend fun getAllProducts(): ProductListResponse {


        return Api.apiService.getAllProducts()
    }

    override suspend fun getProductsByCollectionId(collectionId: Long): ListProductsResponse {
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
            response =  wholeResponse.customer
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

    override suspend fun newGetCustomerByEmail(email: String): Flow<Customer>? {
        var response: Flow<Customer>? = null
        try{
            val customerListMaybe= apiService.getCustomerByEmail(email)
            if (customerListMaybe.customers.isNotEmpty()){
                response = flowOf(customerListMaybe.customers[0])
            }
        }catch (e:Exception){
            e.printStackTrace()
            Log.d("rfGetCustException",e.message.toString())
        }
        return response
    }

    override suspend fun getCustomerById(customerId: Long): Customer? {
        var response: Customer? = null
        try {
            val wholeResponse = apiService.getCustomerById(customerId)
            Log.d("getCustomerById", wholeResponse.customer.toString())
            response =  wholeResponse.customer
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }


    override suspend fun getCustomerMetafields(customerId: Long): List<MetaFieldResponse> {
        var listOfCustomerMetafields: List<MetaFieldResponse> = emptyList()
        try {
            val wholeResponse = apiService.getCustomerMetafields(customerId)
            listOfCustomerMetafields = wholeResponse.metafields
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return listOfCustomerMetafields

    }

    override suspend fun newCreateDraftOrder(draftOrder: DraftOrderRequest): DraftOrderResponse? {
        var response: DraftOrderResponse? = null
        try {
            val wholeResponse = apiService.newCreateDraftOrder(draftOrder)
            response =  wholeResponse
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfCreateDraftException",e.message.toString())

        }
        Log.d("retrofitCreateDraft", response?.draft_order?.id.toString())
        return response
    }

    override suspend fun updateCustomerMetafield(
        customerId: Long,
        customer: MetaFieldCustomerRequest
    ) {
        try {
            apiService.updateCustomerMetafield(customerId, customer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override suspend fun createDraftOrder(draftResponse:DraftOrderResponse): DraftOrder? {
        var response: DraftOrder? = null
        try {
            val wholeResponse = Api.apiService.createDraftOrder(draftResponse)
            response =  wholeResponse.draft_order
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfCreateDraftException",e.message.toString())

        }
        Log.d("retrofitCreateDraft", response?.id.toString())
        return response
    }
    override suspend fun getDraftOrder(id:String): DraftOrder?{
        var response: DraftOrder? = null
        try {
            val wholeResponse = apiService.getDraftOrder(id.toLong())
            Log.d("getDraftRFT", wholeResponse.toString())
            response =  wholeResponse.draft_order
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfGetDraftException",e.message.toString())

        }
        Log.d("retrofitgetDraft", response?.id.toString())
        return response
    }
    override suspend fun updateDraftOrder(draftOrderId : Long , draftResponse:DraftOrderResponse): DraftOrder? {
        var response: DraftOrder? = null
        try {
            val wholeResponse = Api.apiService.updateDraftOrder(draftOrderId,draftResponse)
            response =  wholeResponse.draft_order
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfUpdateDraftException",e.message.toString())

        }
        Log.d("retrofitUpdateDraft", response?.id.toString())
        return response
    }

    override suspend fun getSingleProduct(id:Long): Product?{
        var response: Product? = null
        try {
            val wholeResponse = apiService.getSingleProduct(id)
            Log.d("getProductRFTTry", wholeResponse.toString())
            response =  wholeResponse.product
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfGetProductException",e.message.toString())

        }
        Log.d("retrofitgetProduct", response?.id.toString())
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