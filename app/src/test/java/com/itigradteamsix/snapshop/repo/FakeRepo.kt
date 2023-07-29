package com.itigradteamsix.snapshop.repo

import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.authentication.signup.model.ApiCustomerState
import com.itigradteamsix.snapshop.data.models.Address
import com.itigradteamsix.snapshop.data.models.AddressBody
import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.model.CreateOrderResponse
import com.itigradteamsix.snapshop.model.Customer
import com.itigradteamsix.snapshop.model.ListProductsResponse
import com.itigradteamsix.snapshop.model.OrderResponse
import com.itigradteamsix.snapshop.model.Product
import com.itigradteamsix.snapshop.model.ProductListResponse
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.model.SmartCollectionResponse
import com.itigradteamsix.snapshop.model.SmartCollectionsResponse
import kotlinx.coroutines.flow.Flow

class FakeRepo : RepoInterface {
    override suspend fun getAllProducts(): Flow<ProductListResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getANumberOfProducts(limit: Int): Flow<ProductListResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsByCollectionId(id: Long): Flow<ListProductsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getSmartCollectionById(id: Long): Flow<SmartCollectionResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getSmartCollections(): Flow<SmartCollectionsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftResponse: DraftOrderResponse
    ): Flow<DraftOrder?> {
        TODO("Not yet implemented")
    }

    override suspend fun newGetCustomerByEmail(email: String): Flow<Customer>? {
        TODO("Not yet implemented")
    }

    override suspend fun createCustomer(customer: CustomerResponse): ApiCustomerState {
        TODO("Not yet implemented")
    }

    override suspend fun getSingleProduct(id: Long): Flow<Product?> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllAddresses(customer_id: String): Flow<List<Address>?> {
        TODO("Not yet implemented")
    }

    override suspend fun addNewAddress(customer_id: String, address: AddressBody): Flow<Address?> {
        TODO("Not yet implemented")
    }

    override suspend fun removeAddress(address_id: String, customer_id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun makeAddressDefault(customer_id: String, address_id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createOrder(draftOrderId: Long): Flow<CreateOrderResponse?> {
        TODO("Not yet implemented")
    }

    override suspend fun getOrders(email: String): Flow<OrderResponse> {
        TODO("Not yet implemented")
    }
}