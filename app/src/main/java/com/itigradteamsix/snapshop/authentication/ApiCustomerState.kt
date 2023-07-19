package com.itigradteamsix.snapshop.authentication

import com.itigradteamsix.snapshop.data.models.Customer

sealed class ApiCustomerState{

    data class Success(val customerData: Customer?) : ApiCustomerState()
    data class Failure(val exception: Exception) : ApiCustomerState()
    object Loading : ApiCustomerState()

}
