package com.itigradteamsix.snapshop.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.data.models.Customer
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.utils.ConnectionUtil
import com.itigradteamsix.snapshop.utils.ConnectionUtil.performNetworkCheck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProfileViewModel(private val repoInterface: RepoInterface) : ViewModel() {
    private val TAG = "ProfileViewModel"

    private var _customer = MutableStateFlow<ApiState>(ApiState.Loading)
    val customer: StateFlow<ApiState> = _customer


    fun getCustomerByEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
//            val customerApiState = repoInterface.newGetCustomerByEmail(email)
            performNetworkCheck(repoInterface.newGetCustomerByEmail(email), MyApplication.appContext)
                ?.catch { e ->
                    _customer.value = ApiState.Failure(e.message.toString())
                }?.collect {
                    _customer.value = it
                }
        }
    }
}