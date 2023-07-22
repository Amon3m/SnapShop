package com.itigradteamsix.snapshop.authentication.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.authentication.ApiCustomerLoginState
import com.itigradteamsix.snapshop.authentication.ApiCustomerState
import com.itigradteamsix.snapshop.authentication.ApiDraftLoginState
import com.itigradteamsix.snapshop.authentication.AuthState
import com.itigradteamsix.snapshop.authentication.login.model.ApiCustomerLoginState
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.authentication.signup.model.AuthState
import com.itigradteamsix.snapshop.authentication.FirebaseRepoInterface
import com.itigradteamsix.snapshop.authentication.signup.model.SignupUser
import com.itigradteamsix.snapshop.model.Customer
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.settings.data.UserPreferences
import com.itigradteamsix.snapshop.settings.data.dataStore
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

    //add user to datastore to avoid login again
    fun addUserToDataStore(isGuest : Boolean , user : Customer?) {
        viewModelScope.launch {
            if (isGuest){
                MyApplication.appInstance.settingsStore.updateUserPreferences(UserPreferences(
                    isFirstTime = false,
                    isLoggedIn = false,
                    isGuest = true,
                    customerId= 0,
                    customerName = "",
                    customerEmail = "",
                    userCurrency = "usd",
                ))
            }else{
                MyApplication.appInstance.settingsStore.updateUserPreferences(UserPreferences(
                    isFirstTime = false,
                    isLoggedIn = true,
                    isGuest = false,
                    customerId= user?.id!!,
                    customerName = user.first_name + " " + user.last_name,
                    customerEmail = user.email!!,
                    userCurrency = "usd",
                ))
            }
        }
    }

}