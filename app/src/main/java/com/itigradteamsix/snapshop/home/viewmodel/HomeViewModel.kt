package com.itigradteamsix.snapshop.home.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel (private val repoInterface: RepoInterface) : ViewModel() {

    private val _smartCollection = MutableStateFlow<ApiState>(ApiState.Loading)
    val smartCollection: StateFlow<ApiState>
        get()=_smartCollection

    private val _productList = MutableStateFlow<ApiState>(ApiState.Loading)
    val productList: StateFlow<ApiState>
        get() = _productList

    fun getSmartCollections(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNetworkAvailable(context)){
                repoInterface.getSmartCollections().catch {e->
                    _smartCollection.emit(ApiState.Failure(e.message ?: ""))
                }.collect{

                    _smartCollection.emit(ApiState.Success(it))
                }
            }else{_smartCollection.emit(ApiState.Failure( "internet connection not found" +
                    ""))}
        }}
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
    fun getAllProducts(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNetworkAvailable(context)){
                repoInterface.getAllProducts().catch { e ->
                    _productList.emit(ApiState.Failure(e.message ?: "")) }.collect{
                    var products=it

                    _productList.emit(ApiState.Success(products))}
            }else{
                Log.e("fail","fail")
                _productList.emit(ApiState.Failure("eeeeeee"))}
        }
    }


}