package com.itigradteamsix.snapshop.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.settings.data.UserPreferences
import com.itigradteamsix.snapshop.utils.ConnectionUtil.performNetworkCheck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProfileViewModel(private val repoInterface: RepoInterface) : ViewModel() {
    private val TAG = "ProfileViewModel"

    private var _customer = MutableStateFlow<ApiState>(ApiState.Loading)
    val customer: StateFlow<ApiState> = _customer

    //prefernce flow to check if user is logged in or not
    val userPreferencesFlow = MyApplication.appInstance.settingsStore.userPreferencesFlow

    private var _lastTwoOrders = MutableStateFlow<ApiState>(ApiState.Loading)
    val lastTwoOrders: StateFlow<ApiState> = _lastTwoOrders


    fun getCustomerByEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
//            val customerApiState = repoInterface.newGetCustomerByEmail(email)
            performNetworkCheck(repoInterface.newGetCustomerByEmail(email), MyApplication.appContext)
                .catch { e ->
                    _customer.value = ApiState.Failure(e.message.toString())
                }.collect {
                    _customer.value = it
                }
        }
    }

    //on Signout
    fun removeUserFromDataStore(){
        viewModelScope.launch {
            MyApplication.appInstance.settingsStore.updateUserPreferences(UserPreferences(isFirstTime = false,false,false,0,"","","usd"))
        }
    }

//    fun getLastTwoOrders() {
//        viewModelScope.launch(Dispatchers.IO) {
//            performNetworkCheck(repoInterface.getLastTwoOrders(), MyApplication.appContext)
//                .catch { e ->
//                    _lastTwoOrders.value = ApiState.Failure(e.message.toString())
//                }.collect {
//                    _lastTwoOrders.value = it
//                }
//        }
//    }


}