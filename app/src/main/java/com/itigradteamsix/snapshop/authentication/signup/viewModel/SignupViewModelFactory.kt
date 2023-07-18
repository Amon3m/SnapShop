package com.itigradteamsix.snapshop.authentication.signup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itigradteamsix.snapshop.authentication.FirebaseRepoInterface
import com.itigradteamsix.snapshop.authentication.login.viewModel.LoginViewModel

class SignupViewModelFactory (private val iRepo: FirebaseRepoInterface
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return SignupViewModel(iRepo ) as T
        }
        throw IllegalArgumentException("Class Not found")
    }
}