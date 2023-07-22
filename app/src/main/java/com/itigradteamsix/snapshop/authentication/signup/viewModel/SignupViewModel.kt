package com.itigradteamsix.snapshop.authentication.signup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.authentication.APiDraftState
import com.itigradteamsix.snapshop.authentication.ApiCustomerState
import com.itigradteamsix.snapshop.authentication.AuthState
import com.itigradteamsix.snapshop.authentication.FirebaseRepoInterface
import com.itigradteamsix.snapshop.authentication.login.model.CustomerResponse
import com.itigradteamsix.snapshop.authentication.signup.model.SignupUser
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignupViewModel(private val iRepo: FirebaseRepoInterface) : ViewModel() {
    private val _signupResultFlow : MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    val signupResultFlow: StateFlow<AuthState> = _signupResultFlow

    private val _createCustomerResultFlow : MutableStateFlow<ApiCustomerState> = MutableStateFlow(ApiCustomerState.Loading)
    val createCustomerResultFlow: StateFlow<ApiCustomerState> = _createCustomerResultFlow

    private val _createDraftFlow: MutableStateFlow<APiDraftState> = MutableStateFlow(APiDraftState.Loading)
    val createDraftFlow: StateFlow<APiDraftState> = _createDraftFlow


    fun signUpUser(user: SignupUser) {
        viewModelScope.launch {
            _signupResultFlow.value = iRepo.signUpUser(user)
        }
    }
    fun createCustomer(customer: CustomerResponse){

        viewModelScope.launch {
            _createCustomerResultFlow.value = iRepo.createCustomer(customer)
        }
    }
    fun createDraftOrder(draftOrderResponse: DraftOrderResponse ) {
        viewModelScope.launch {
            _createDraftFlow.value= iRepo.createDraftOrder(draftOrderResponse)
        }
    }

}