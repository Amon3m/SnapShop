package com.itigradteamsix.snapshop.model


import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.authentication.signup.model.ApiCustomerState
import com.itigradteamsix.snapshop.data.models.Address
import com.itigradteamsix.snapshop.data.models.AddressBody
import com.itigradteamsix.snapshop.database.LocalSource
import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class Repository private constructor(
    var remoteSource: RemoteSource, var concreteLocalSource: LocalSource
) : RepoInterface {

    companion object {
        private var instance: Repository? = null
        fun getInstance(
            remoteSource: RemoteSource,
            concreteLocalSource: LocalSource
        ): Repository {
            return instance ?: synchronized(this) {
                val temp = Repository(remoteSource, concreteLocalSource)
                instance = temp
                temp
            }
        }
    }


    override suspend fun getAllProducts():Flow<ProductListResponse> {
        return flowOf(remoteSource.getAllProducts())
    }

    override suspend fun getANumberOfProducts(limit: Int):Flow<ProductListResponse> {
        return flowOf(remoteSource.getANumberOfProducts(limit))
    }
    override suspend fun getProductsByCollectionId(id: Long): Flow<ListProductsResponse> {
        return flowOf(remoteSource.getProductsByCollectionId(id))
    }

    override suspend fun getSmartCollectionById(id: Long): Flow<SmartCollectionResponse> {
        return flowOf(remoteSource.getSmartCollectionById(id))
    }
    override suspend fun getSmartCollections(): Flow<SmartCollectionsResponse> {
        return flowOf(remoteSource.getSmartCollections())}


    override suspend fun getSomeListFromDatabase(): Flow<List<String>> {
        return concreteLocalSource.getSomeListFromDatabase()

    }
    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftResponse: DraftOrderResponse
    ): Flow<DraftOrder?> {
        return flowOf(remoteSource.updateDraftOrder(draftOrderId,draftResponse))

    }


    override suspend fun createCustomer(customer: CustomerResponse): ApiCustomerState {
        return ApiCustomerState.Success(remoteSource.createCustomer(customer))
    }

    override suspend fun newGetCustomerByEmail(email: String): Flow<Customer>? {
        return remoteSource.newGetCustomerByEmail(email)
    }

    override suspend fun getSingleProduct(id:Long): Flow<Product?> {
        return flowOf( remoteSource.getSingleProduct(id))

    }

    override suspend fun getAllAddresses(customer_id: String): Flow<List<Address>?> {
        return flowOf( remoteSource.getAllAddresses(customer_id))
    }

    override suspend fun addNewAddress(customer_id: String, address: AddressBody): Flow<Address?> {
        return flowOf( remoteSource.addNewAddress(customer_id,address))
    }

    override suspend fun removeAddress(address_id: String, customer_id: String) {
        remoteSource.removeAddress(address_id,customer_id)
    }
    override suspend fun makeAddressDefault( customer_id: String,address_id: String) {
        remoteSource.makeAddressDefault(customer_id,address_id)
    }
    override suspend fun createOrder(draftOrderId: Long): Flow<CreateOrderResponse?> {
        return flowOf(remoteSource.createOrder(draftOrderId))
    }

    override suspend fun getOrders(email:String): Flow<OrderResponse> {
        return flowOf(remoteSource.getOrders(email))
    }


}