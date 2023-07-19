package com.itigradteamsix.snapshop.authentication

import com.itigradteamsix.snapshop.authentication.signup.model.SignupUser
import com.itigradteamsix.snapshop.data.models.Customer

interface FirebaseRepoInterface {
    suspend fun loginUser(email: String,pass:String): AuthState?
    suspend fun signUpUser(user: SignupUser): AuthState
    suspend fun createCustomer(customer: Customer) : Customer


}