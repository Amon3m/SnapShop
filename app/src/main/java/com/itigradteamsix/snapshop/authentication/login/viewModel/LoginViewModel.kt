package com.itigradteamsix.snapshop.authentication.login.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.authentication.AuthState
import com.itigradteamsix.snapshop.authentication.FirebaseRepoInterface
import com.itigradteamsix.snapshop.authentication.signup.model.SignupUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(val iRepo: FirebaseRepoInterface)  : ViewModel() {
    private val _loginResultFlow : MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    val loginResultFlow: MutableStateFlow<AuthState> = _loginResultFlow
    fun loginUser(email : String , password :String) {
        viewModelScope.launch {
            _loginResultFlow.value= iRepo.loginUser(email,password)!!
//                (iRepo.loginUser(email,password) ?:AuthState.Success(false))
        }
    }
}