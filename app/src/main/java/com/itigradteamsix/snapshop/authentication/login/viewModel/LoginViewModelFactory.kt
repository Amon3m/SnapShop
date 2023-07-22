package com.itigradteamsix.snapshop.authentication.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itigradteamsix.snapshop.authentication.FirebaseRepoInterface

class LoginViewModelFactory(private val iRepo: FirebaseRepoInterface,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(iRepo ) as T
        }
        throw IllegalArgumentException("Class Not found")
    }
}