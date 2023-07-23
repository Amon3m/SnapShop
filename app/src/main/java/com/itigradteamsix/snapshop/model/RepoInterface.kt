package com.itigradteamsix.snapshop.model

import com.itigradteamsix.snapshop.authentication.ApiCustomerLoginState
import com.itigradteamsix.snapshop.authentication.ApiCustomerState
import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.http.Path

interface RepoInterface {
    suspend fun getAllProducts(): Flow<ProductListResponse>

    suspend fun getANumberOfProducts(limit: Int): Flow<ProductListResponse>
    suspend fun getProductsByCollectionId(id: Long): Flow<ListProductsResponse>

    suspend fun getSmartCollectionById(id: Long): Flow<SmartCollectionResponse>
    suspend fun getSmartCollections(): Flow<SmartCollectionsResponse>


    suspend fun getSomeListFromDatabase(): Flow<List<String>>


    //Hamza (make it return a flow of customer)
    suspend fun newGetCustomerByEmail(email: String) : Flow<Customer>?

    suspend fun createCustomer(customer: CustomerResponse) : ApiCustomerState

    suspend fun updateCustomerMetafield(customerId: Long, customer: MetaFieldCustomerRequest)

    suspend fun getCustomerById(customerId: Long): Customer?

    suspend fun getCustomerMetafields(customerId: Long): List<MetaFieldResponse>

    suspend fun newCreateDraftOrder(draftOrder: DraftOrderRequest): DraftOrderResponse?





}
