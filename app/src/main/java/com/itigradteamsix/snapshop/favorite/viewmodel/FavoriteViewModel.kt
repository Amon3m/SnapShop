package com.itigradteamsix.snapshop.favorite.viewmodel

import androidx.lifecycle.ReportFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.authentication.FirebaseRepoInterface
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.model.RepoInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
            _getDraftFlow.value= iRepo2.getDraftOrder(id)
        }
    }
}