package com.itigradteamsix.snapshop.address.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itigradteamsix.snapshop.authentication.FirebaseRepoInterface
import com.itigradteamsix.snapshop.authentication.login.viewModel.LoginViewModel
import com.itigradteamsix.snapshop.model.RepoInterface

class AddressViewModelFactory (private val iRepo: RepoInterface,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
            return AddressViewModel(iRepo ) as T
        }
        throw IllegalArgumentException("Class Not found")
    }
}