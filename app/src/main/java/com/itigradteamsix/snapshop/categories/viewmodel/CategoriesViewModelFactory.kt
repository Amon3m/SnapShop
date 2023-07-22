package com.itigradteamsix.snapshop.categories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itigradteamsix.snapshop.model.RepoInterface

class CategoriesViewModelFactory(private val _repo: RepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass::class.java.isInstance(CategoriesViewModel::class.java)) {
            CategoriesViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("View Model class not found")
        }
    }
}