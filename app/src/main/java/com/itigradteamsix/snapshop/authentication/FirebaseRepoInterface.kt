package com.itigradteamsix.snapshop.authentication

import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.authentication.signup.model.APiDraftState
import com.itigradteamsix.snapshop.authentication.login.model.ApiCustomerLoginState
import com.itigradteamsix.snapshop.authentication.signup.model.ApiCustomerState
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.authentication.signup.model.AuthState
import com.itigradteamsix.snapshop.authentication.signup.model.SignupUser
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse

interface FirebaseRepoInterface {
    suspend fun loginUser(email: String,pass:String): AuthState?
    suspend fun signUpUser(user: SignupUser): AuthState
    suspend fun createCustomer(customer: CustomerResponse) : ApiCustomerState
    suspend fun getCustomerByEmail(email: String) : ApiCustomerLoginState
    suspend fun createDraftOrder(draftOrderResponse: DraftOrderResponse): APiDraftState
    suspend fun getDraftOrder(id : String): ApiDraftLoginState



}