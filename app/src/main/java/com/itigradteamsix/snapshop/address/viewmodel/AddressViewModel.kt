package com.itigradteamsix.snapshop.address.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.Utilities
import com.itigradteamsix.snapshop.data.models.AddressBody
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddressViewModel(val iRepo: RepoInterface)  : ViewModel() {


    private val _addressFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    val addressFlow: StateFlow<ApiState> = _addressFlow
    private val _newAddressFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    val newAddressFlow: StateFlow<ApiState> = _newAddressFlow

    fun getALlAddresses(id: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (Utilities.isNetworkAvailable(context)) {
                iRepo.getAllAddresses(id).catch { e ->
                    _addressFlow.emit(ApiState.Failure(e.message ?: ""))
                }.collect {

                    _addressFlow.emit(ApiState.Success(it))
                }
            } else {
                _addressFlow.emit(
                    ApiState.Failure(
                        "internet connection not found" +
                                ""
                    )
                )
            }
        }
    }

    fun addNewAddress(id: String, context: Context, address: AddressBody) {
        viewModelScope.launch(Dispatchers.IO) {
            if (Utilities.isNetworkAvailable(context)) {
                iRepo.addNewAddress(id, address).catch { e ->
                    _newAddressFlow.emit(ApiState.Failure(e.message ?: ""))
                }.collect {
                    _newAddressFlow.emit(ApiState.Success(it))
                }
            } else {
                _addressFlow.emit(
                    ApiState.Failure(
                        "internet connection not found" +
                                ""
                    )
                )
            }
        }
    }

    fun removeAddress(id: String, customer_id: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            iRepo.removeAddress(id, customer_id)
        }
    }
}