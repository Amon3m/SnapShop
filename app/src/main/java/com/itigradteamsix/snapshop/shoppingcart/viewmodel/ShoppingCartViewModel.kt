package com.itigradteamsix.snapshop.shoppingcart.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiState

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ShoppingCartViewModel(private val repoInterface: RepoInterface):  ViewModel() {

    init {
     //   getSmartCollections()
    }

    private val _productList = MutableStateFlow<ApiState>(ApiState.Loading)
    val productList: StateFlow<ApiState>
        get() = _productList


    private val _smartCollection = MutableStateFlow<ApiState>(ApiState.Loading)
    val smartCollection: StateFlow<ApiState>
        get()=_smartCollection


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




    fun getSmartCollections(context: Context) {

        viewModelScope.launch(Dispatchers.IO) {
            if (isNetworkAvailable(context)){
          repoInterface.getSmartCollectionById(453400101165).catch {e->
              _smartCollection.emit(ApiState.Failure(e.message ?: ""))
          }.collect{

              _smartCollection.emit(ApiState.Success(it))
          }
        }else{_smartCollection.emit(ApiState.Failure( "fail;"))}
    }}
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


}