package com.itigradteamsix.snapshop.model


import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.authentication.signup.model.ApiCustomerState
import com.itigradteamsix.snapshop.data.models.Address
import com.itigradteamsix.snapshop.data.models.AddressBody
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface RepoInterface {
    suspend fun getAllProducts(): Flow<ProductListResponse>

    suspend fun getANumberOfProducts(limit: Int): Flow<ProductListResponse>
    suspend fun getProductsByCollectionId(id: Long): Flow<ListProductsResponse>

    suspend fun getSmartCollectionById(id: Long): Flow<SmartCollectionResponse>
    suspend fun getSmartCollections(): Flow<SmartCollectionsResponse>


    suspend fun updateDraftOrder(draftOrderId : Long , draftResponse: DraftOrderResponse): Flow<DraftOrder?>



    //Hamza (make it return a flow of customer)
    suspend fun newGetCustomerByEmail(email: String) : Flow<Customer>?

    suspend fun createCustomer(customer: CustomerResponse) : ApiCustomerState
    suspend fun getSingleProduct(id:Long): Flow<Product?>
    suspend fun getAllAddresses(customer_id: String): Flow<List<Address>?>
    suspend fun addNewAddress(customer_id:String, address: AddressBody): Flow<Address?>
    suspend fun removeAddress(address_id:String,customer_id:String)
    suspend fun makeAddressDefault( customer_id: String,address_id: String)

    suspend fun createOrder(draftOrderId : Long ): Flow<CreateOrderResponse?>

    suspend fun getOrders(email:String): Flow<OrderResponse>




}
