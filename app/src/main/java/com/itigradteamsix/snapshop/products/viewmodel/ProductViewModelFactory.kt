package com.itigradteamsix.snapshop.products.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itigradteamsix.snapshop.model.RepoInterface

class ProductViewModelFactory (private val _repo: RepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass::class.java.isInstance(ProductViewModel::class.java)) {
            ProductViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("View Model class not found")
        }
    }
}