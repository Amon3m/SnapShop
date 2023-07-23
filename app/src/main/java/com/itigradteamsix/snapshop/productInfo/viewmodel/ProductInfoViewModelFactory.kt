package com.itigradteamsix.snapshop.productInfo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.favorite.viewmodel.FavoriteViewModel
import com.itigradteamsix.snapshop.model.RepoInterface

class ProductInfoViewModelFactory (private val iRepo: RepoInterface,private val iRepo2: FirebaseRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductInfoViewModel::class.java)) {
            return ProductInfoViewModel(iRepo,iRepo2 ) as T
        }
        throw IllegalArgumentException("Class Not found")
    }
}