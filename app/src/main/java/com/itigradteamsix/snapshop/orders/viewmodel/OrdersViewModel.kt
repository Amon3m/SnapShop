package com.itigradteamsix.snapshop.orders.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.Utilities
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class OrdersViewModel(private val repo: RepoInterface) : ViewModel()  {

    private val _orders = MutableStateFlow<ApiState>(ApiState.Loading)
    val orders: StateFlow<ApiState>
        get()=_orders


    fun getCustomerOrders(context: Context,email:String) {
        val isNetworkAvailable = Utilities.isNetworkAvailable(context)
        viewModelScope.launch(Dispatchers.IO) {
            if (isNetworkAvailable){
                repo.getOrders(email).catch {e->
                    _orders.emit(ApiState.Failure(e.message ?: ""))
                }.collect{

                    _orders.emit(ApiState.Success(it))
                }
            }else{_orders.emit(
                ApiState.Failure( "internet connection not found" +
                    ""))}
        }}
}