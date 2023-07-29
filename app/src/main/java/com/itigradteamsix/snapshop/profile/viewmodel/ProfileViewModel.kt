package com.itigradteamsix.snapshop.profile.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.Utilities
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.settings.data.UserPreferences
import com.itigradteamsix.snapshop.utils.ConnectionUtil.performNetworkCheck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class ProfileViewModel(private val repoInterface: RepoInterface) : ViewModel() {
    private val TAG = "ProfileViewModel"

    private var _customer = MutableStateFlow<ApiState>(ApiState.Loading)
    val customer: StateFlow<ApiState> = _customer


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
            MyApplication.appInstance.settingsStore.updateUserPreferences(UserPreferences(isFirstTime = false,false,true,0,"","","usd",0L,0L))
        }
    }

    fun getLastTwoOrders(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.getOrders(email)
                .catch { e ->
                    _lastTwoOrders.value = ApiState.Failure(e.message.toString())
                }
                .collect {
                    Log.d(TAG, "getLastTwoOrders OrderResponse: $it")
                    val lastTwoOrders = it.orders?.take(2)
                    Log.d(TAG, "getLastTwoOrders: $lastTwoOrders")
                    _lastTwoOrders.value = ApiState.Success(lastTwoOrders)
                }
        }
    }


    fun getCustomerOrders(context: Context, email:String) {
        val isNetworkAvailable = Utilities.isNetworkAvailable(context)
        viewModelScope.launch(Dispatchers.IO) {
            if (isNetworkAvailable){
                repoInterface.getOrders(email).catch {e->
                    _lastTwoOrders.emit(ApiState.Failure(e.message ?: ""))
                }.collect{
                    Log.d(TAG, "getCustomerOrders: $it")

                    _lastTwoOrders.emit(ApiState.Success(it.orders))

                }
            }else{_lastTwoOrders.emit(
                ApiState.Failure( "internet connection not found" +
                        ""))}
        }}


}