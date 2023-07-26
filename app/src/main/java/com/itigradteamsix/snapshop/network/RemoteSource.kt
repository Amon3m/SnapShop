package com.itigradteamsix.snapshop.network

import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.data.models.Address
import com.itigradteamsix.snapshop.data.models.AddressBody
import com.itigradteamsix.snapshop.data.models.AddressResponse
import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.model.Customer
import com.itigradteamsix.snapshop.model.ListProductsResponse
import com.itigradteamsix.snapshop.model.Product
import com.itigradteamsix.snapshop.model.ProductListResponse
import com.itigradteamsix.snapshop.model.SmartCollection
import com.itigradteamsix.snapshop.model.SmartCollectionResponse
import com.itigradteamsix.snapshop.model.SmartCollectionsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.Path

interface RemoteSource {

//    suspend fun getWeatherFromNetwork(
//        lat: Double=0.0,
//        lon: Double=0.0,
//        exclude:String="",
//        units:String="",
//        lang:String="",
//        appid:String="ccb811f49ff661e0a43e8d8727e0387a"
//    ): WeatherResponse
    suspend fun getAllProducts(): ProductListResponse
    suspend fun getProductsByCollectionId(collectionId: Long): ListProductsResponse
    suspend fun getANumberOfProducts(number: Int): ProductListResponse
    suspend fun getSmartCollectionById(id: Long): SmartCollectionResponse
    suspend fun getSmartCollections(): SmartCollectionsResponse
    suspend fun createCustomer(customer: CustomerResponse): Customer?
    suspend fun getCustomerByEmail(email: String): List<Customer>?
    suspend fun createDraftOrder(draftResponse: DraftOrderResponse): DraftOrder?
    suspend fun getDraftOrder(id:String): DraftOrder?
    suspend fun updateDraftOrder(draftOrderId : Long , draftResponse:DraftOrderResponse): DraftOrder?

    suspend fun newGetCustomerByEmail(email: String): Flow<Customer>?
    suspend fun getSingleProduct(id:Long): Product?
    suspend fun getAllAddresses(customer_id: String): List<Address>?
    suspend fun addNewAddress(customer_id:String, address: AddressBody):Address?
    suspend fun removeAddress(address_id:String,customer_id:String)
    suspend fun makeAddressDefault(customer_id:String,address_id:String)




}
