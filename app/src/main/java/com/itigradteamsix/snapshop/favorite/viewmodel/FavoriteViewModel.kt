package com.itigradteamsix.snapshop.favorite.viewmodel

import androidx.lifecycle.ReportFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.authentication.FirebaseRepoInterface
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.settings.currency.CurrencyUtils.convertCurrency
import com.itigradteamsix.snapshop.settings.currency.CurrencyUtils.convertCurrencyWithoutSymbol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.Exception

class FavoriteViewModel(val iRepo: RepoInterface , val iRepo2: FirebaseRepo)  : ViewModel() {


    private val _getDraftFlow : MutableStateFlow<ApiDraftLoginState> = MutableStateFlow(
        ApiDraftLoginState.Loading)
    val getDraftFlow: StateFlow<ApiDraftLoginState> = _getDraftFlow

    fun updateDraftOrder(draftOrderId: Long, draftResponse: DraftOrderResponse) {
        viewModelScope.launch {
            iRepo.updateDraftOrder(draftOrderId, draftResponse)
        }
    }

    fun getDraftOrder(id : String ) {
        viewModelScope.launch {
            val currencyPreferences = MyApplication.appInstance.settingsStore.currencyPreferencesFlow.first()

            val draftOrder = iRepo2.getDraftOrder(id)
            //make when statement
            when(draftOrder){
                is ApiDraftLoginState.Success -> {
                    //change currency on draft order line items
                    draftOrder.data?.line_items?.forEach {
                        it.price = convertCurrency(it.price?.toDoubleOrNull(), currencyPreferences)
                    }
                    if(draftOrder.data != null){
                        _getDraftFlow.value = ApiDraftLoginState.Success(draftOrder.data)
                    }else{
                        _getDraftFlow.value = ApiDraftLoginState.Failure(Exception("NO INTERNET"))
                    }



                }
                is ApiDraftLoginState.Failure -> {
//                    _getDraftFlow.value = ApiDraftLoginState.Failure(draftOrder.exception)
                }
                is ApiDraftLoginState.Loading -> {
//                    _getDraftFlow.value = ApiDraftLoginState.Loading
                }
            }

        }
    }
}