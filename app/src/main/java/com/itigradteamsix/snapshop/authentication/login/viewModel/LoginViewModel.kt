package com.itigradteamsix.snapshop.authentication.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.authentication.login.model.ApiCustomerLoginState
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.authentication.signup.model.AuthState
import com.itigradteamsix.snapshop.authentication.FirebaseRepoInterface
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(val iRepo: FirebaseRepoInterface)  : ViewModel() {
    private val _loginResultFlow : MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    val loginResultFlow: StateFlow<AuthState> = _loginResultFlow


    private val _customerByEmailFlow : MutableStateFlow<ApiCustomerLoginState> = MutableStateFlow(
        ApiCustomerLoginState.Loading)
    val customerByEmailFlow: StateFlow<ApiCustomerLoginState> = _customerByEmailFlow


    private val _getDraftFlow : MutableStateFlow<ApiDraftLoginState> = MutableStateFlow(
        ApiDraftLoginState.Loading)
    val getDraftFlow: StateFlow<ApiDraftLoginState> = _getDraftFlow


    fun loginUser(email : String , password :String) {
        viewModelScope.launch {
            _loginResultFlow.value= iRepo.loginUser(email,password)!!
//                (iRepo.loginUser(email,password) ?:AuthState.Success(false))
        }
    }

    fun getCustomerByEmail(email : String ) {
        viewModelScope.launch {
            _customerByEmailFlow.value= iRepo.getCustomerByEmail(email)
        }
    }
    fun getDraftOrder(id : String ) {
        viewModelScope.launch {
            _getDraftFlow.value= iRepo.getDraftOrder(id)
        }
    }

}