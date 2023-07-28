package com.itigradteamsix.snapshop.authentication.signup.model

import com.itigradteamsix.snapshop.model.Customer

sealed class ApiCustomerState{

    data class Success(val customerData: Customer?) : ApiCustomerState()
    data class Failure(val exception: Exception) : ApiCustomerState()
    object Loading : ApiCustomerState()

}
