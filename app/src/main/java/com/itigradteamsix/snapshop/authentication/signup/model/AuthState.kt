package com.itigradteamsix.snapshop.authentication.signup.model

sealed class AuthState{
    data class Success(val data:Boolean) : AuthState()
    data class Failure(val exception: Exception) : AuthState()
    object Loading : AuthState()
}
