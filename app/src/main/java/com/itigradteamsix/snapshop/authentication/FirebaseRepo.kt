package com.itigradteamsix.snapshop.authentication

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.authentication.signup.model.APiDraftState
import com.itigradteamsix.snapshop.authentication.login.model.ApiCustomerLoginState
import com.itigradteamsix.snapshop.authentication.signup.model.ApiCustomerState
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.authentication.signup.model.AuthState
import com.itigradteamsix.snapshop.authentication.signup.model.SignupUser
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.network.ApiClient
import kotlinx.coroutines.tasks.await

class FirebaseRepo (val auth : FirebaseAuth) : FirebaseRepoInterface {
    private val retrofit : ApiClient = ApiClient
    override suspend fun loginUser(email: String, pass: String): AuthState? {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            if (auth.currentUser?.isEmailVerified == false) {
//                auth.currentUser?.sendEmailVerification()?.await()

                Log.d("insideLoginRepo","email verification sent")
                return AuthState.Success(false) //TODO MAKE IT FALSE
            }
            auth.currentUser?.isEmailVerified?.let { AuthState.Success(true) }

        } catch (e: Exception) {
            AuthState.Failure(e)
        }
    }

    override suspend fun signUpUser(user: SignupUser): AuthState {
        return try {
            auth.createUserWithEmailAndPassword(user.email, user.password).await()
            auth.currentUser?.sendEmailVerification()?.await()
            AuthState.Success(true)
        } catch (e: Exception) {
            AuthState.Failure(e)
        }
    }

    override suspend fun createCustomer(customer: CustomerResponse): ApiCustomerState {
       return ApiCustomerState.Success(retrofit.createCustomer(customer))
    }

    override suspend fun getCustomerByEmail(email: String): ApiCustomerLoginState {
        return ApiCustomerLoginState.Success(retrofit.getCustomerByEmail(email))
    }

    override suspend fun createDraftOrder(draftOrderResponse: DraftOrderResponse): APiDraftState {
        return APiDraftState.Success(retrofit.createDraftOrder(draftOrderResponse))
    }
    override suspend fun getDraftOrder(id : String): ApiDraftLoginState {
        return ApiDraftLoginState.Success(retrofit.getDraftOrder(id))
    }




}