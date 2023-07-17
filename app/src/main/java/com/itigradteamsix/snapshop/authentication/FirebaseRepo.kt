package com.itigradteamsix.snapshop.authentication

import com.google.firebase.auth.FirebaseAuth
import com.itigradteamsix.snapshop.authentication.signup.model.SignupUser
import kotlinx.coroutines.tasks.await

class FirebaseRepo (val auth : FirebaseAuth) : FirebaseRepoInterface {
    override suspend fun loginUser(email: String, pass: String): AuthState? {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            if (auth.currentUser?.isEmailVerified == false) {
                auth.currentUser?.sendEmailVerification()?.await()
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


}