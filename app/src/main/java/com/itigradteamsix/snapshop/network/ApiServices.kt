package com.itigradteamsix.snapshop.data.repository.remote

import com.itigradteamsix.snapshop.model.ProductListResponse
import com.itigradteamsix.snapshop.model.SmartCollectionResponse
import com.itigradteamsix.snapshop.model.SmartCollectionsResponse

import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.authentication.login.model.CustomersLoginResponse
import com.itigradteamsix.snapshop.data.models.Address
import com.itigradteamsix.snapshop.data.models.AddressBody
import com.itigradteamsix.snapshop.data.models.AddressResponse
import com.itigradteamsix.snapshop.data.models.CustomerAddressResponse
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.model.ListProductsResponse
import com.itigradteamsix.snapshop.model.ProductResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    //https://shopify.dev/docs/api/admin-rest/2023-07/resources/product#get-products
    //Retrieve list of all products ✅
    @GET("products.json")
    suspend fun getAllProducts(): ProductListResponse

    //Retrieve a number of products ✅
    @GET("products.json")
    suspend fun getANumberOfProducts(@Query("limit") number: Int): ProductListResponse

    //Retrieve all products that belong to a certain collection ✅
    @GET("products.json")
    suspend fun getProductsByCollectionId(@Query("collection_id") id: Long): ListProductsResponse

    //these are all the brands ✅
    @GET("smart_collections.json")
    suspend fun getSmartCollections(): SmartCollectionsResponse

    //get Only one smart collection (brand) by id
    @GET("smart_collections/{id}.json")
    suspend fun getSmartCollectionById(@Path("id") id: Long): SmartCollectionResponse
    //used in signup fragment to create the customer by the info added
    @POST("customers.json")
    suspend fun createCustomer(@Body customer: CustomerResponse): CustomerResponse
    //used in login to get the pojo of the customer by the email inserted
    @GET("customers/search.json")
    suspend fun getCustomerByEmail(@Query("email") email:String): CustomersLoginResponse
    @POST("draft_orders.json")
    suspend fun createDraftOrder(@Body draftOrder: DraftOrderResponse): DraftOrderResponse
    @GET("draft_orders/{draft_order_id}.json")
    suspend fun getDraftOrder(@Path(value = "draft_order_id")draftOrderId:Long): DraftOrderResponse
    @PUT("draft_orders/{draft_order_id}.json")
    suspend fun updateDraftOrder(@Path(value = "draft_order_id")draftOrderId:Long,
                                    @Body draftOrder: DraftOrderResponse
    ): DraftOrderResponse
    @GET("products/{product_id}.json")
    suspend fun getSingleProduct(@Path("product_id") product_id: Long): ProductResponse

    @GET("customers/{customer_id}/addresses.json")
    suspend fun getAllAddresses(@Path("customer_id") customer_id: String): AddressResponse

    @POST("customers/{customer_id}/addresses.json")
    suspend fun addNewAddressForUser(@Path(value="customer_id") customer_id:String, @Body address: AddressBody):CustomerAddressResponse
    @DELETE("customers/{customer_id}/addresses/{address_id}.json")
    suspend fun removeAddress(@Path(value="address_id")address_id:String,@Path(value="customer_id")customer_id:String)
    @POST("customers/{customer_id}/addresses/{address_id}/default.json")
    suspend fun makeAddressDefault(@Path(value="customer_id") customer_id:String, @Path(value="address_id") address_id:String) :CustomerAddressResponse
}