package com.itigradteamsix.snapshop.brands.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itigradteamsix.snapshop.home.viewmodel.HomeViewModel
import com.itigradteamsix.snapshop.model.RepoInterface

class BrandsViewModelFactory(private val _repo: RepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass::class.java.isInstance(BrandsViewModel::class.java)) {
            BrandsViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("View Model class not found")
        }
    }
}
