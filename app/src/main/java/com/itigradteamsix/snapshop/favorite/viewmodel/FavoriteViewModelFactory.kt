package com.itigradteamsix.snapshop.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.authentication.FirebaseRepoInterface
import com.itigradteamsix.snapshop.authentication.login.viewModel.LoginViewModel
import com.itigradteamsix.snapshop.model.RepoInterface

class FavoriteViewModelFactory(private val iRepo: RepoInterface,private val iRepo2: FirebaseRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(iRepo,iRepo2 ) as T
        }
        throw IllegalArgumentException("Class Not found")
    }
}