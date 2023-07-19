package com.itigradteamsix.snapshop.authentication

import com.itigradteamsix.snapshop.data.models.Customer

sealed class ApiCustomerLoginState{

    data class Success(val customerData: List<Customer>?) : ApiCustomerLoginState()
    data class Failure(val exception: Exception) : ApiCustomerLoginState()
    object Loading : ApiCustomerLoginState()

}

