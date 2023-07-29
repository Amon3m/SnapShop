package com.itigradteamsix.snapshop.home.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.model.Customer
import com.itigradteamsix.snapshop.model.DraftOrder
import com.itigradteamsix.snapshop.model.DraftOrderRequest
import com.itigradteamsix.snapshop.model.LineItem
import com.itigradteamsix.snapshop.model.MetaFieldCustomerInput
import com.itigradteamsix.snapshop.model.MetaFieldCustomerRequest
import com.itigradteamsix.snapshop.model.MetafieldInput
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.model.CreateDraftOrder
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(private val repoInterface: RepoInterface) : ViewModel() {

    private val _smartCollection = MutableStateFlow<ApiState>(ApiState.Loading)
    val smartCollection: StateFlow<ApiState>
        get()=_smartCollection

    init {
        checkForDraftOrder()


    }

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
//                _productList.emit(ApiState.Failure("eeeeeee"))
            }
        }
    }

    //check if current customer has a cart draft order and adds one if not, also registers the draft order id in Metafield and in settings store
    private fun checkForDraftOrder() {
        viewModelScope.launch {
            MyApplication.appInstance.settingsStore.userPreferencesFlow.collectLatest {
//                val customer = repoInterface.getCustomerById(it.customerId)
                if (!it.isGuest && it.cartDraftOrderId == 0L) {
                    val metaFields = repoInterface.getCustomerMetafields(it.customerId)
                    if (metaFields.isEmpty() || metaFields.last().value.equals("newvalue")) {
                        Log.d("HomeViewModel", "checkForDraftOrder: no metafields")
                        //no draft order is here so create one and get its id
                        val createdDraftOrderResponse = repoInterface.newCreateDraftOrder(
                            DraftOrderRequest(
                                CreateDraftOrder(
                                    name = "cart_draft",
                                    line_items = listOf(
                                        LineItem(
                                            title = "empty",
                                            quantity = 1,
                                            price = "0"
                                        )
                                    ),
                                    customer = Customer(id = it.customerId) //to send confirmation email
                                )
                            )
                        )
                        Log.d(
                            "HomeViewModel",
                            "checkForDraftOrder: created draft order response $createdDraftOrderResponse"
                        )
                        val draftOrderId = createdDraftOrderResponse?.draft_order?.id

                        if (draftOrderId != null) {
                            MyApplication.appInstance.settingsStore.updateCartDraftOrderId(
                                draftOrderId
                            )
                            repoInterface.updateCustomerMetafield(
                                it.customerId, MetaFieldCustomerRequest(
                                    MetaFieldCustomerInput(
                                        it.customerId,
                                        listOf(
                                            MetafieldInput(
                                                "draft_order_id",
                                                value = "$draftOrderId",
                                                "single_line_text_field",
                                                "global"
                                            )
                                        )
                                    )
                                )
                            )
                            repoInterface.getCustomerMetafields(it.customerId)
                                .last().id.let { it1 ->
                                    MyApplication.appInstance.settingsStore.updateMetaFieldId(
                                        it1
                                    )
                                    checkForDraftOrder()
                                }

                        } else {
                            Log.d("HomeViewModel", "checkForDraftOrder: draft order id is null")
                        }
                    } else {
                        //log all metafields
                        Log.d("HomeViewModel", "checkForDraftOrder: ${metaFields.last().value}")
                        MyApplication.appInstance.settingsStore.updateCartDraftOrderId(metaFields.last().value.toLong())
                        MyApplication.appInstance.settingsStore.updateMetaFieldId(metaFields.last().id)
                    }
                } else {
                    Log.d("HomeViewModel", "stored Draft Order: ${it.cartDraftOrderId}")
                    Log.d("HomeViewModel", "stored Metafield Id: ${it.metaFieldId}")

                }


            }

        }
    }


}