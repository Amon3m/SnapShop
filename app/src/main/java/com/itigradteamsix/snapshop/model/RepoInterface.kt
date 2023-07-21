package com.itigradteamsix.snapshop.model

import com.itigradteamsix.snapshop.authentication.ApiCustomerLoginState
import com.itigradteamsix.snapshop.authentication.ApiCustomerState
import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.data.models.Customer
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface RepoInterface {
    suspend fun getAllProducts(): Flow<ProductListResponse>

    suspend fun getANumberOfProducts(limit: Int): Flow<ProductListResponse>
    suspend fun getProductsByCollectionId(id: Long): Flow<ProductListResponse>

    suspend fun getSmartCollectionById(id: Long): Flow<SmartCollectionResponse>
    suspend fun getSmartCollections(): Flow<SmartCollectionsResponse>


    suspend fun getSomeListFromDatabase(): Flow<List<String>>


    //Hamza (make it return a flow of customer)
    suspend fun newGetCustomerByEmail(email: String) : Flow<Customer>?

    suspend fun createCustomer(customer: CustomerResponse) : ApiCustomerState


}
