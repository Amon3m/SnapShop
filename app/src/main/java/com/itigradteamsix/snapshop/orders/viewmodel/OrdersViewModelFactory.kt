package com.itigradteamsix.snapshop.orders.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itigradteamsix.snapshop.model.RepoInterface

class OrdersViewModelFactory(private val _repo: RepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass::class.java.isInstance(OrdersViewModel::class.java)) {
            OrdersViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("View Model class not found")
        }
    }
}