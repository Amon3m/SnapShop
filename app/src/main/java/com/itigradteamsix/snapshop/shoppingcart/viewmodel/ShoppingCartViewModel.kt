package com.itigradteamsix.snapshop.shoppingcart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiState

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ShoppingCartViewModel(private val repoInterface: RepoInterface):  ViewModel() {

    init {
        getAllProducts()
        getSmartCollections()
    }

    private val _productList = MutableStateFlow<ApiState>(ApiState.Loading)
    val productList: StateFlow<ApiState>
        get() = _productList


    private val _smartCollection = MutableStateFlow<ApiState>(ApiState.Loading)
    val smartCollection: StateFlow<ApiState>
        get()=_smartCollection


    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.getAllProducts().catch { e ->
                _productList.emit(ApiState.Failure(e.message ?: "")) }.collect{
                var products=it
                _productList.emit(ApiState.Success(products))

            }

        }
    }

    fun getSmartCollections() {
        viewModelScope.launch(Dispatchers.IO) {
          repoInterface.getSmartCollectionById(453400101165).catch {e->
              _smartCollection.emit(ApiState.Failure(e.message ?: ""))
          }.collect{
              _smartCollection.emit(ApiState.Success(it))
          }
        }
    }



}