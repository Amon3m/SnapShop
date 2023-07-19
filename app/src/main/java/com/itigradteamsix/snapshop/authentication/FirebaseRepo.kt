package com.itigradteamsix.snapshop.authentication

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.authentication.signup.model.SignupUser
import com.itigradteamsix.snapshop.data.models.Customer
import com.itigradteamsix.snapshop.data.repository.remote.ShopifyApiClient
import kotlinx.coroutines.tasks.await

class FirebaseRepo (val auth : FirebaseAuth) : FirebaseRepoInterface {
    private val retrofit : ShopifyApiClient = ShopifyApiClient
    override suspend fun loginUser(email: String, pass: String): AuthState? {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            if (auth.currentUser?.isEmailVerified == false) {
//                auth.currentUser?.sendEmailVerification()?.await()

                Log.d("insideLoginRepo","email verification sent")
                return AuthState.Success(false)
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


}