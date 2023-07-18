package com.itigradteamsix.snapshop.shoppingcart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.data.models.Product
import com.itigradteamsix.snapshop.data.models.SmartCollection
import com.itigradteamsix.snapshop.data.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShoppingCartViewModel(private val shopRepository: ShopRepository):  ViewModel() {
    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> = _productList

    private val _smartCollection = MutableStateFlow<SmartCollection?>(null)
    val smartCollection: StateFlow<SmartCollection?> = _smartCollection

    fun getAllProducts() {
        viewModelScope.launch {
            val productList = shopRepository.getAllProducts()
            if (productList != null) {
                _productList.value = productList
            }
        }
    }


    fun getSmartCollections() {
        viewModelScope.launch {
            val smartCollection = shopRepository.getSmartCollectionById(453400101165) //Adidas
            if (smartCollection != null) {
                _smartCollection.value = smartCollection
            }
        }
    }



}