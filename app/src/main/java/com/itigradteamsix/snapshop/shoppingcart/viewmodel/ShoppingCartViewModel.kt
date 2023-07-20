package com.itigradteamsix.snapshop.shoppingcart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.model.Product
import com.itigradteamsix.snapshop.model.SmartCollection
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiStateProductList
import com.itigradteamsix.snapshop.network.ApiStateSmartCollection
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

    private val _productList = MutableStateFlow<ApiStateProductList>(ApiStateProductList.Loading)
    val productList: StateFlow<ApiStateProductList>
        get() = _productList


    private val _smartCollection = MutableStateFlow<ApiStateSmartCollection>(ApiStateSmartCollection.Loading)
    val smartCollection: StateFlow<ApiStateSmartCollection>
        get()=_smartCollection


    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.getAllProducts().catch { e ->
                _productList.emit(ApiStateProductList.Failure(e.message ?: "")) }.collect{
                var products=it
                _productList.emit(ApiStateProductList.Success(products))

            }

        }
    }

    fun getSmartCollections() {
        viewModelScope.launch(Dispatchers.IO) {
          repoInterface.getSmartCollectionById(453400101165).catch {e->
              _smartCollection.emit(ApiStateSmartCollection.Failure(e.message ?: ""))
          }.collect{
              _smartCollection.emit(ApiStateSmartCollection.Success(it))
          }
        }
    }



}