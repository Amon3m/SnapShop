package com.itigradteamsix.snapshop.fakedatasources

import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
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
import com.itigradteamsix.snapshop.model.SmartCollectionResponse
import com.itigradteamsix.snapshop.model.SmartCollectionsResponse
import com.itigradteamsix.snapshop.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRemoteDataSource(
    var draftOrder: DraftOrder
    , var customers:MutableList<Customer>
    , var product: Product
    , var orderResponse: OrderResponse
    , var draftOrders: MutableList<com.itigradteamsix.snapshop.favorite.model.DraftOrder>
    , var productList:MutableList<Product>
) : RemoteSource {
    override suspend fun getAllProducts(): ProductListResponse {
        return ProductListResponse(productList)
    }

    override suspend fun getProductsByCollectionId(collectionId: Long): ListProductsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getANumberOfProducts(number: Int): ProductListResponse {
        if (productList.size >= number) {
            return ProductListResponse(productList.subList(0, number))
        } else {
            return ProductListResponse(productList)
        }
    }


    override suspend fun createCustomer(customer: CustomerResponse): Customer? {
        return Customer(customer.customer.id)
    }

    override suspend fun getCustomerByEmail(email: String): List<Customer>? {
        val response = customers
        response[0].email = email
        return response
    }

    override suspend fun createDraftOrder(draftResponse: DraftOrderResponse): DraftOrder? {
        return draftResponse.draft_order!!.copy()
    }

    override suspend fun getDraftOrder(id: String): DraftOrder? {
        val response = draftOrder.copy(id = id.toLong())
        return response as  DraftOrder
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftResponse: DraftOrderResponse
    ): DraftOrder? {
        for (i in draftOrders.indices) {
            if (draftOrders[i].id == draftOrderId) {
                draftOrders[i] = draftResponse.draft_order!!
                return draftOrders[i] as DraftOrder

            }
        }
        return null
    }


    override suspend fun newGetCustomerByEmail(email: String): Flow<Customer>? {
        val response = customers
        response[0].email = email
        return flowOf( response[0] )   }

    override suspend fun getSingleProduct(id: Long): Product? {
        val response = product.copy(id = id.toLong())
        return response
    }

    override suspend fun getAllAddresses(customer_id: String): List<Address>? {
        for (customer in customers) {
            if (customer.id == customer_id.toLong()) {
                return customer.addresses
            }
        }
        return emptyList()
    }

    override suspend fun addNewAddress(customer_id: String, address: AddressBody): Address? {
        for (customer in customers) {
            if (customer.id == customer_id.toLong()) {
                val list: MutableList<Address> = customer.addresses as MutableList<Address>
                list.add(address.address)
                customer.addresses = list
                return address.address
            }
        }
        return null
    }

    override suspend fun removeAddress(address_id: String, customer_id: String) {
        for (customer in customers) {
            if (customer.id == customer_id.toLong()) {
                val list: MutableList<Address> = customer.addresses as MutableList<Address>
                for (item in list) {
                    if (item.id == address_id.toLong()) {
                        list.remove(item)
                        customer.addresses = list
                        break
                    }
                }
            }
        }
    }

    override suspend fun makeAddressDefault(customer_id: String, address_id: String) {
        for (customer in customers) {
            if (customer.id == customer_id.toLong()) {
                val list: List<Address> = customer.addresses as MutableList<Address>
                for (item in list) {
                    if (item.id == address_id.toLong())
                    {   item.default=true
                        customer.addresses = list
                    }
                }
            }
        }
    }

    override suspend fun createOrder(draftOrderId: Long): CreateOrderResponse? {
//        var order = OrdersItem(id = draftOrderId)
        return CreateOrderResponse(com.itigradteamsix.snapshop.model.DraftOrder(id = draftOrderId))
    }

    override suspend fun getOrders(email: String): OrderResponse {
        val response = orderResponse.copy()
        response.orders!![0]!!.email = email
        return response
    }

    override suspend fun getSmartCollectionById(id: Long): SmartCollectionResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getSmartCollections(): SmartCollectionsResponse {
        TODO("Not yet implemented")
    }
}