package com.itigradteamsix.snapshop.brands.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.Utilities
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class BrandsViewModel (private val repo: RepoInterface) : ViewModel()  {



    private val _brands = MutableStateFlow<ApiState>(ApiState.Loading)
    val brands: StateFlow<ApiState>
        get()=_brands


    fun getSmartCollections(context: Context) {
        val isNetworkAvailable = Utilities.isNetworkAvailable(context)
        viewModelScope.launch(Dispatchers.IO) {
            if (isNetworkAvailable){
                repo.getSmartCollections().catch {e->
                    _brands.emit(ApiState.Failure(e.message ?: ""))
                }.collect{

                    _brands.emit(ApiState.Success(it))
                }
            }else{_brands.emit(ApiState.Failure( "internet connection not found" +
                    ""))}
        }}
}