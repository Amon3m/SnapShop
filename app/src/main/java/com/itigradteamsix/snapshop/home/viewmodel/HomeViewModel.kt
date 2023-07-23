package com.itigradteamsix.snapshop.home.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.model.DraftOrder
import com.itigradteamsix.snapshop.model.DraftOrderRequest
import com.itigradteamsix.snapshop.model.LineItem
import com.itigradteamsix.snapshop.model.MetaFieldCustomerInput
import com.itigradteamsix.snapshop.model.MetaFieldCustomerRequest
import com.itigradteamsix.snapshop.model.MetafieldInput
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(private val repoInterface: RepoInterface) : ViewModel() {

    private val _smartCollection = MutableStateFlow<ApiState>(ApiState.Loading)
    val smartCollection: StateFlow<ApiState>
        get() = _smartCollection

    init {
        checkForDraftOrder()
    }

    fun getSmartCollections(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNetworkAvailable(context)) {
                repoInterface.getSmartCollections().catch { e ->
                    _smartCollection.emit(ApiState.Failure(e.message ?: ""))
                }.collect {

                    _smartCollection.emit(ApiState.Success(it))
                }
            } else {
                _smartCollection.emit(
                    ApiState.Failure(
                        "internet connection not found" +
                                ""
                    )
                )
            }
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    //check if current customer has a cart draft order and adds one if not, also registers the draft order id in Metafield and in settings store
    private fun checkForDraftOrder() {
        viewModelScope.launch {
            MyApplication.appInstance.settingsStore.userPreferencesFlow.collectLatest {
//                val customer = repoInterface.getCustomerById(it.customerId)
                if (!it.isGuest) {
                    val metaFields = repoInterface.getCustomerMetafields(it.customerId)
                    if (metaFields.isEmpty()) {
                        Log.d("HomeViewModel", "checkForDraftOrder: no metafields")
                        //no draft order is here so create one and get its id
                        val createdDraftOrderResponse = repoInterface.newCreateDraftOrder(
                            DraftOrderRequest(
                                DraftOrder(
                                    name = "cart_draft",
                                    line_items = listOf(
                                        LineItem(
                                            title = "test",
                                            quantity = 1,
                                            price = "0"
                                        )
                                    )
                                )
                            )
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
                        }
                    } else {
                        MyApplication.appInstance.settingsStore.updateCartDraftOrderId(metaFields[0].value.toLong())
                    }
                }


            }

        }
    }


}