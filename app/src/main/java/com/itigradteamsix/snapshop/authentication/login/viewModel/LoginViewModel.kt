package com.itigradteamsix.snapshop.authentication.login.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.authentication.ApiCustomerLoginState
import com.itigradteamsix.snapshop.authentication.ApiCustomerState
import com.itigradteamsix.snapshop.authentication.AuthState
import com.itigradteamsix.snapshop.authentication.FirebaseRepoInterface
import com.itigradteamsix.snapshop.authentication.signup.model.SignupUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(val iRepo: FirebaseRepoInterface)  : ViewModel() {
    private val _loginResultFlow : MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    val loginResultFlow: StateFlow<AuthState> = _loginResultFlow


    private val _customerByEmailFlow : MutableStateFlow<ApiCustomerLoginState> = MutableStateFlow(
        ApiCustomerLoginState.Loading)
    val customerByEmailFlow: StateFlow<ApiCustomerLoginState> = _customerByEmailFlow
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
}