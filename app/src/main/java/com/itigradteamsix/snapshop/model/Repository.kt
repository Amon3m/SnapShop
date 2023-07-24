package com.itigradteamsix.snapshop.model


import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.authentication.signup.model.ApiCustomerState
import com.itigradteamsix.snapshop.database.LocalSource
import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.http.Path
//import DraftOrderResponse
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse

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
    override suspend fun getProductsByCollectionId(id: Long):Flow<ListProductsResponse> {
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

    override suspend fun updateCustomerMetafield(
        customerId: Long,
        customer: MetaFieldCustomerRequest
    ) {
        remoteSource.updateCustomerMetafield(customerId, customer)
    }

    override suspend fun newGetCustomerByEmail(email: String): Flow<Customer>? {
        return remoteSource.newGetCustomerByEmail(email)
    }

    override suspend fun getCustomerById(customerId: Long): Customer?{
        return remoteSource.getCustomerById(customerId)
    }

    override suspend fun getCustomerMetafields(customerId: Long): List<MetaFieldResponse> {
        return remoteSource.getCustomerMetafields(customerId)
    }

    override suspend fun newCreateDraftOrder(draftOrder: DraftOrderRequest): DraftOrderResponse? {
        return remoteSource.newCreateDraftOrder(draftOrder)
    }
    override suspend fun getSingleProduct(id:Long): Flow<Product?> {
        return flowOf( remoteSource.getSingleProduct(id))

    }



}