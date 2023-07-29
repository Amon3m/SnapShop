package com.itigradteamsix.snapshop.repo

import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.authentication.signup.model.ApiCustomerState
import com.itigradteamsix.snapshop.data.models.Address
import com.itigradteamsix.snapshop.data.models.AddressBody
import com.itigradteamsix.snapshop.fakedatasources.FakeLocalDataSource
import com.itigradteamsix.snapshop.fakedatasources.FakeRemoteDataSource
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.model.Customer
import com.itigradteamsix.snapshop.model.OrderResponse
import com.itigradteamsix.snapshop.model.OrdersItem
import com.itigradteamsix.snapshop.model.Product
import com.itigradteamsix.snapshop.model.Repository
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class RepositoryTest {

    private lateinit var fakeLocalDataSource: FakeLocalDataSource
    private lateinit var fakeRemoteSource: FakeRemoteDataSource


    private var customer2 = Customer(
        id = 2, email = "mohamed@gmail.com",
    )

    private var address1 = Address(
        id = 1, customer_id = 1, city = "Cairo"
    )
    private var address2 = Address(
        id = 2, customer_id = 1, city = "Assiut"
    )
    private var address3 = Address(
        id = 3, customer_id = 1, city = "Giza"
    )
    private var addressList: MutableList<Address> = mutableListOf(address1, address2)
    private var customer1 = Customer(
        id = 1, email = "ahmed@gmail.com", addresses = addressList
    )

    private var product1 = Product(
        id = 1, title = "Shoe"
    )
    private var product2 = Product(
        id = 2, title = "Shirt"
    )
    private var product3 = Product(
        id = 2, title = "Bag"
    )
    private var draftOrder1 = com.itigradteamsix.snapshop.favorite.model.DraftOrder(
        id = 1, email = "ahmed@gmail.com"
    )
    private var draftOrder2 = com.itigradteamsix.snapshop.favorite.model.DraftOrder(
        id = 2, email = "mohamed@gmail.com"
    )
    private var draftOrder3 = com.itigradteamsix.snapshop.favorite.model.DraftOrder(
        id = 3, email = "ali@gmail.com"
    )
    private var draftOrder4 = com.itigradteamsix.snapshop.favorite.model.DraftOrder(
        id = 4, email = "ali@gmail.com"
    )
    private var order1 = OrdersItem(
        id = 1, email = "mohamed@gmail.com"
    )
    private var order2 = OrdersItem(
        id = 2, email = "mohamed@gmail.com"
    )
    private var orderList: MutableList<OrdersItem> = mutableListOf(order1, order2)
    private var customerList: MutableList<Customer> = mutableListOf(customer1, customer2)
    private var draftOrderList: MutableList<com.itigradteamsix.snapshop.favorite.model.DraftOrder> =
        mutableListOf(draftOrder1, draftOrder2, draftOrder3)
    private var productList: MutableList<Product> = mutableListOf(product1, product2, product3)
    private var orderResponse = OrderResponse(orderList)
    private var draftOrderResponse = DraftOrderResponse(draftOrder4)
    private lateinit var repo: Repository


    @Before
    fun setUp() {
        fakeLocalDataSource = FakeLocalDataSource()
        fakeRemoteSource = FakeRemoteDataSource(
            draftOrder1,
            customerList,
            product1,
            orderResponse,
            draftOrderList,
            productList,
        )
        repo = Repository.getInstance(
            fakeRemoteSource, fakeLocalDataSource
        )
    }

    @Test
    fun getAllProducts_ProductList_sizeEqualThree() = runBlocking {
        // given : List contains Products items

        repo.getAllProducts().collect {
            assertThat(it.products.size, CoreMatchers.`is`(3))
            //when : Getting Products list size
            //then : the result = 3
        }
    }

    @Test
    fun getANumberOfProducts_ProductList_SizeEqualThree() = runBlocking {
        // given : List contains Products items

        repo.getANumberOfProducts(1).collect {
            assertThat(it.products.size, CoreMatchers.`is`(1))
            //when : Getting Products list size with limit 1
            //then : the result = 1
        }
    }


    @Test
    fun updateDraftOrder_resultEqual2() = runBlocking {
        // given : A draft order with specific id

        repo.updateDraftOrder(1, draftOrderResponse).collect {
            assertThat(it!!.id, CoreMatchers.`is`(4))
            //when : updating the draft order with another response
            //then : the result = 4 (given draft order in parameter)
        }
    }


    @Test
    fun createCustomer()= runBlocking {
        // given : A draft order with specific id

       val response= repo.createCustomer(CustomerResponse(customer1)) as ApiCustomerState.Success

        assertThat(customer1.id, CoreMatchers.`is`(response.customerData?.id))
    }

    @Test
    fun newGetCustomerByEmail(): Unit = runBlocking {

        repo.newGetCustomerByEmail(customer1.email!!)?.collect {
            assertThat(it.email, CoreMatchers.`is`(customer1.email))

        }

    }

    @Test
    fun getSingleProduct(): Unit = runBlocking {

        repo.getSingleProduct(product1.id).collect {
            assertThat(it?.id, CoreMatchers.`is`(product1.id))

        }

    }

    @Test
    fun getAllAddresses(): Unit = runBlocking {

        repo.getAllAddresses(customer1.id.toString()).collect {
            assertThat(it?.size, CoreMatchers.`is`(2))

        }

    }

    @Test
    fun addNewAddress(): Unit = runBlocking {

        repo.addNewAddress(customer1.id.toString(), AddressBody(address3)).collect {
            assertThat(it?.id, CoreMatchers.`is`(3))

        }

    }

    @Test
    fun removeAddress(): Unit = runBlocking {

        repo.removeAddress(customer_id = customer1.id.toString(), address_id = "2")
        assertThat(addressList.size, CoreMatchers.`is`(1))


    }

    @Test
    fun makeAddressDefault(): Unit = runBlocking {

        repo.makeAddressDefault(customer_id = customer1.id.toString(), address_id = "2")
        assertThat(address2.default, CoreMatchers.`is`(true))


    }

    @Test
    fun createOrder(): Unit = runBlocking {

        repo.createOrder(draftOrder1.id!!).collect {
            assertThat(it?.draftOrder!!.id, CoreMatchers.`is`(draftOrder1.id))

        }
    }

    @Test
    fun getOrders(): Unit = runBlocking {

        repo.getOrders(draftOrder1.email!!).collect {
            assertThat(it.orders!![0]?.email, CoreMatchers.`is`(draftOrder1.email))

        }
    }
}