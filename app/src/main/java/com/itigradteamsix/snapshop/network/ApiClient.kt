package com.itigradteamsix.snapshop.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.data.repository.remote.ApiServices
import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.model.Customer
import com.itigradteamsix.snapshop.model.Discount
import com.itigradteamsix.snapshop.model.DraftOrderRequest
import com.itigradteamsix.snapshop.model.LineItem
import com.itigradteamsix.snapshop.model.ListProductsResponse
import com.itigradteamsix.snapshop.model.MetaFieldCustomerInput
import com.itigradteamsix.snapshop.model.MetaFieldCustomerRequest
import com.itigradteamsix.snapshop.model.MetaFieldResponse
import com.itigradteamsix.snapshop.model.MetafieldInput
import com.itigradteamsix.snapshop.model.Product
import com.itigradteamsix.snapshop.model.ProductListResponse
import com.itigradteamsix.snapshop.model.SmartCollectionResponse
import com.itigradteamsix.snapshop.model.SmartCollectionsResponse
import com.itigradteamsix.snapshop.model.UpdateMetafieldInput
import com.itigradteamsix.snapshop.model.UpdateMetafieldRequest
import com.itigradteamsix.snapshop.network.Api.apiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://itp-sv-and6.myshopify.com/admin/api/2023-07/"
const val TAG = "ApiClientLOGS"

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

    override suspend fun getSmartCollections(): SmartCollectionsResponse {

        return Api.apiService.getSmartCollections()

    }

    override suspend fun createCustomer(customer: CustomerResponse): Customer? {
        var response: Customer? = null
        try {
            val wholeResponse = apiService.createCustomer(customer)
            response = wholeResponse.customer
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfCreateCustException", e.message.toString())

        }
        Log.d("retrofitCreateCust", response?.email.toString())
        return response
    }

    override suspend fun getCustomerByEmail(email: String): List<Customer>? {
        var response: List<Customer>? = null
        try {
            val wholeResponse = apiService.getCustomerByEmail(email)
            Log.d("retrofitCreateCustres", wholeResponse.toString())
            response = wholeResponse.customers
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfGetCustException", e.message.toString())

        }
        Log.d("retrofitCreateCust", response?.get(0)?.email.toString())
        return response
    }

    override suspend fun newGetCustomerByEmail(email: String): Flow<Customer>? {
        var response: Flow<Customer>? = null
        try {
            val customerListMaybe = apiService.getCustomerByEmail(email)
            if (customerListMaybe.customers.isNotEmpty()) {
                response = flowOf(customerListMaybe.customers[0])
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfGetCustException", e.message.toString())
        }
        return response
    }

    override suspend fun getCustomerById(customerId: Long): Customer? {
        var response: Customer? = null
        try {
            val wholeResponse = apiService.getCustomerById(customerId)
            Log.d("getCustomerById", wholeResponse.customer.toString())
            response = wholeResponse.customer
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

    suspend fun updateDraftOrderInCustomerMetaField(metafieldId: Long, newDraftOrderId: String, customerId: Long){
        try {
            // Create the metafield request with the updated value
            val metafieldRequest = UpdateMetafieldRequest(
                metafield = UpdateMetafieldInput(
                    id = metafieldId,
                    value = newDraftOrderId,
                    type = "single_line_text_field"
                )
            )

            // Make the PUT request to update the customer metafield
            val metafieldResponse = apiService.newUpdateCustomerMetafield(customerId, metafieldId, metafieldRequest)
            println("Metafield updated successfully: ${metafieldResponse.metafield}")

        } catch (e: Exception) {
            println("Error updating metafield: ${e.message}")
        }
    }


    override suspend fun updateCustomerMetafield(
        customerId: Long,
        customer: MetaFieldCustomerRequest
    ) {
        try {
            apiService.createCustomerMetafield(customerId, customer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override suspend fun createDraftOrder(draftResponse: DraftOrderResponse): DraftOrder? {
        var response: DraftOrder? = null
        try {
            val wholeResponse = Api.apiService.createDraftOrder(draftResponse)
            response = wholeResponse.draft_order
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfCreateDraftException", e.message.toString())

        }
        Log.d("retrofitCreateDraft", response?.id.toString())
        return response
    }

    override suspend fun getDraftOrder(id: String): DraftOrder? {
        var response: DraftOrder? = null
        try {
            val wholeResponse = apiService.getDraftOrder(id.toLong())
            Log.d("getDraftRFT", wholeResponse.toString())
            response = wholeResponse.draft_order
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfGetDraftException", e.message.toString())

        }
        Log.d("retrofitgetDraft", response?.id.toString())
        return response
    }


    override suspend fun newCreateDraftOrder(draftOrder: DraftOrderRequest): DraftOrderResponse? {
        var response: DraftOrderResponse? = null
        try {
            val wholeResponse = apiService.newCreateDraftOrder(draftOrder)
            response = wholeResponse
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfCreateDraftException", e.message.toString())

        }
        Log.d("retrofitCreateDraft", response?.draft_order?.id.toString())
        return response
    }

    //complete a draft order and create a  new draft order then return its id and register it in the datastore
    suspend fun completeDraftOrder(draftOrderId: Long): Boolean {
        var newDraftOrderId: Long? = 0L
        var orderSuccess = false
        try {
            apiService.completeDraftOrder(draftOrderId)
            val newDraftOrderResponse = apiService.newCreateDraftOrder(DraftOrderRequest(com.itigradteamsix.snapshop.model.DraftOrder(
                name = "cart_draft",
                line_items = listOf(
                    LineItem(
                    title = "empty",
                    quantity = 1,
                    price = "0"
                )
            ))))

            newDraftOrderId = newDraftOrderResponse.draft_order?.id

            MyApplication.appInstance.settingsStore.userPreferencesFlow.collectLatest {
                //register it in the metafield and datastore
                MyApplication.appInstance.settingsStore.updateCartDraftOrderId(
                    newDraftOrderId!!
                )
                orderSuccess = true
//                delay(2000)
                Log.d("newDraftOrderId", " draft id $newDraftOrderId + customer ${it.customerId} + metafield ${it.metaFieldId}")
//                updateCustomerMetafield(
//                    it.customerId, MetaFieldCustomerRequest(
//                        MetaFieldCustomerInput(
//                            it.customerId,
//                            listOf(
//                                MetafieldInput(
//                                    "draft_order_id",
//                                    value = "$newDraftOrderId",
//                                    "single_line_text_field",
//                                    "global"
//                                )
//                            )
//                        )
//                    )
//                )

//                updateDraftOrderInCustomerMetaField(it.metaFieldId, newDraftOrderId.toString(), it.customerId) //TODO uncomment
                orderSuccess = true

            }


        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfCompleteDraftException", e.message.toString())
            orderSuccess = false

        }
        Log.d("newDraftOrderId", newDraftOrderId.toString())
        return orderSuccess
    }

    //apply discount to the draft order
     suspend fun completeDraftOrderWithDiscount(appliedDiscount: Discount,draftOrderId: Long){
//        try {
//            // Create the applied discount object
//            val appliedDiscount = Discount(
//                description = "Custom discount",
//                value_type = "percentage",
//                value = "10.0",
//                amount = "19.90",
//                title = "Custom"
//            )
//
//            // Create the draft order request with the updated applied discount
//            val draftOrderRequest = DraftOrderRequest(
//                draft_order = DraftOrderInput(
//                    id = draftOrderId,
//                    applied_discount = appliedDiscount
//                )
//            )
//
//            // Make the PUT request to update the draft order
//            val draftOrderResponse = shopifyApi.updateDraftOrder(draftOrderId, draftOrderRequest)
//            println("Draft order updated successfully: ${draftOrderResponse.draft_order}")
//
//        } catch (e: Exception) {
//            println("Error applying discount on draft order: ${e.message}")
//        }
     }


    suspend fun getDraftOrderAsFlow(id: Long): Flow<DraftOrder>? {
        var response: Flow<DraftOrder>? = null
        try {
            val draftOrderMaybe = apiService.getDraftOrder(id)
            if (draftOrderMaybe.draft_order != null) {
                response = flowOf(draftOrderMaybe.draft_order!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, e.message.toString())
        }
        return response

    }

    //pass the new modifiedDraftOrder in DraftOrderResponse wrapper
    suspend fun updateDraftOrderWithNewItems(
        draftOrderId: Long,
        modifiedDraftOrder: DraftOrderResponse
    ): DraftOrder? {
        var response: DraftOrder? = null
        try {
            val wholeResponse = Api.apiService.updateDraftOrder(draftOrderId, modifiedDraftOrder)
            response = wholeResponse.draft_order
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfUpdateDraftException", e.message.toString())

        }
        Log.d("retrofitUpdateDraft", response?.id.toString())
        return response
    }


    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftResponse: DraftOrderResponse
    ): DraftOrder? {
        var response: DraftOrder? = null
        try {
            val wholeResponse = Api.apiService.updateDraftOrder(draftOrderId, draftResponse)
            response = wholeResponse.draft_order
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfUpdateDraftException", e.message.toString())

        }
        Log.d("retrofitUpdateDraft", response?.id.toString())
        return response
    }

    override suspend fun getSingleProduct(id: Long): Product? {
        var response: Product? = null
        try {
            val wholeResponse = apiService.getSingleProduct(id)
            Log.d("getProductRFTTry", wholeResponse.toString())
            response = wholeResponse.product
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("rfGetProductException", e.message.toString())

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
            //wait for 600 ms between successive requests
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
        .addInterceptor(RateLimitInterceptor())
        .build()
}

object Api {
    val apiService: ApiServices by lazy {
        RetrofitHelper.retrofitInstance.create(ApiServices::class.java)
    }
}