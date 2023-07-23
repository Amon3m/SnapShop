package com.itigradteamsix.snapshop.productInfo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.Utilities.isNetworkAvailable
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductInfoViewModel(private val repoInterface: RepoInterface,private val iRepo: FirebaseRepo) : ViewModel() {

    private val _productFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    val productFlow: StateFlow<ApiState>  = _productFlow

    fun getSingleProduct(id : Long , context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNetworkAvailable(context)) {
                repoInterface.getSingleProduct(id).catch { e ->
                    _productFlow.emit(ApiState.Failure(e.message ?: ""))
                }.collect {

                    _productFlow.emit(ApiState.Success(it))
                }
            } else {
                _productFlow.emit(
                    ApiState.Failure(
                        "internet connection not found" +
                                ""
                    )
                )
            }
        }
    }

    private val _getDraftFlow : MutableStateFlow<ApiDraftLoginState> = MutableStateFlow(
        ApiDraftLoginState.Loading)
    val getDraftFlow: StateFlow<ApiDraftLoginState> = _getDraftFlow

    fun updateDraftOrder(draftOrderId: Long, draftResponse: DraftOrderResponse) {
        viewModelScope.launch {
            repoInterface.updateDraftOrder(draftOrderId, draftResponse)
        }
    }

    fun getDraftOrder(id : String ) {
        viewModelScope.launch {
            _getDraftFlow.value= iRepo.getDraftOrder(id)
        }
    }
}