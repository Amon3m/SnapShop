package com.itigradteamsix.snapshop.authentication.signup.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.authentication.AuthState
import com.itigradteamsix.snapshop.authentication.FirebaseRepoInterface
import com.itigradteamsix.snapshop.authentication.signup.model.SignupUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignupViewModel(private val iRepo: FirebaseRepoInterface) : ViewModel() {
    private val _signupResultFlow : MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Loading)
    val signupResultFlow: MutableStateFlow<AuthState> = _signupResultFlow
    fun signUpUser(user: SignupUser) {
        viewModelScope.launch {
            _signupResultFlow.value = iRepo.signUpUser(user)
        }
    }
}