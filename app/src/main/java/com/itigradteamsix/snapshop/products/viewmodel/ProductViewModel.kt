package com.itigradteamsix.snapshop.products.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.Utilities
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.settings.currency.CurrencyUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProductViewModel(private val repo: RepoInterface) : ViewModel() {

    private val _products = MutableStateFlow<ApiState>(ApiState.Loading)
    val products: StateFlow<ApiState>
        get() = _products


    fun getProducts(context: Context, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val currencyPreferences = MyApplication.appInstance.settingsStore.currencyPreferencesFlow.first()

            val isNetworkAvailable = Utilities.isNetworkAvailable(context)
            if (isNetworkAvailable) {
                repo.getProductsByCollectionId(id).catch { e ->
                    _products.emit(ApiState.Failure(e.message ?: ""))
                }.collect {

                    //HAMZA: I added this to convert the currency
                    it.products?.forEach { product ->
                        product?.variants?.forEach { variant ->
                            variant?.price = CurrencyUtils.convertCurrencyWithoutSymbol( //symbol is added in the adapter
                                variant?.price,
                                currencyPreferences
                            )
                        }
                    }

                    _products.emit(ApiState.Success(it))
                }
            } else {
                _products.emit(ApiState.Failure("internet connection not found"))
            }
        }
    }
}
