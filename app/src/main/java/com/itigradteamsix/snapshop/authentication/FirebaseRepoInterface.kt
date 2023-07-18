package com.itigradteamsix.snapshop.authentication

import com.itigradteamsix.snapshop.authentication.signup.model.SignupUser

interface FirebaseRepoInterface {
    suspend fun loginUser(email: String,pass:String): AuthState?
    suspend fun signUpUser(user: SignupUser): AuthState

}