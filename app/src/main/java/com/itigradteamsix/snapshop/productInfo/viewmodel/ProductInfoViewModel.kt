package com.itigradteamsix.snapshop.productInfo.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.Utilities.isNetworkAvailable
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.favorite.model.LineItemProperty
import com.itigradteamsix.snapshop.favorite.model.LineItems
import com.itigradteamsix.snapshop.model.DraftOrderRequest
import com.itigradteamsix.snapshop.model.LineItem
import com.itigradteamsix.snapshop.model.Product
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.settings.currency.CurrencyUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.Exception


class ProductInfoViewModel(
    private val repoInterface: RepoInterface,
    private val iRepo: FirebaseRepo
) : ViewModel() {

    private val _productFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    val productFlow: StateFlow<ApiState> = _productFlow

    private val _currentCartDraftOrder = MutableStateFlow<ApiState>(ApiState.Loading)
    val currentCartDraftOrder: StateFlow<ApiState> = _currentCartDraftOrder

    //a Triple of product id and quantity the user selected, in order to update the cart with it onCleared()
    private val _currentProductIdAndQuantityIsAdded =
        MutableStateFlow<Triple<Long?, Int?, Boolean?>>(Triple(null, 1, null))
    val currentProductIdAndQuantityIsAdded: StateFlow<Triple<Long?, Int?, Boolean?>> =
        _currentProductIdAndQuantityIsAdded

    init {
//        getCurrentCartDraftOrder()
    }

    private fun getCurrentCartDraftOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            MyApplication.appInstance.settingsStore.userPreferencesFlow.collect {
                if (it.cartDraftOrderId != 0L) {
                    Log.d("cartDraftOrderInDetails:", "STORED draftOrderId: ${it.cartDraftOrderId}")
                    val cartDraftOrder = ApiClient.getDraftOrder(it.cartDraftOrderId.toString())
                    //if the product is currently in the cart, set the quantity to the quantity in the cart
                    currentProductIdAndQuantityIsAdded.value.first?.let { productId ->
                        cartDraftOrder?.line_items?.forEach { lineItem ->
                            Log.d(
                                "cartDraftOrderInDetails:",
                                "lineItemID: ${lineItem.product_id} but productId: $productId"
                            )
                            if (lineItem.product_id == productId) {
                                changeProductCartState(productId, lineItem.quantity, true)
                            }
                        }
                    }
                    Log.d("cartDraftOrderInDetails", cartDraftOrder.toString())
                    _currentCartDraftOrder.emit(ApiState.Success(cartDraftOrder))
                } else {
                    Log.d("cartDraftOrderInDetails", "MAYBE IN GUEST MODE")
                    //TODO: disable add to cart button
                    _currentCartDraftOrder.emit(ApiState.Failure("FUCKING_GUEST"))
                }
            }

        }
    }


    fun changeProductCartState(productId: Long?, quantity: Int?, isAdded: Boolean?) {
        viewModelScope.launch {

            val currentValue = _currentProductIdAndQuantityIsAdded.value


            val newProductId = productId ?: currentValue.first
            val newQuantity = quantity?: currentValue.second
            val newIsAdded = isAdded ?: currentValue.third


            _currentProductIdAndQuantityIsAdded.value =
                Triple(newProductId, newQuantity, newIsAdded)
        }
    }


    override fun onCleared() {
        super.onCleared()

    }

    fun updateCart() {
        viewModelScope.launch {
            val currentValue = currentProductIdAndQuantityIsAdded.value
            if (currentCartDraftOrder.value is ApiState.Success<*>) {
                val draftOrder =
                    (currentCartDraftOrder.value as ApiState.Success<*>).data as DraftOrder
                //Log DraftOrder id
                Log.d("cartDraftOrderInDetails:", "draftOrderId: ${draftOrder.id}")
                val lineItems = draftOrder.line_items
                val newLineItems = mutableListOf<LineItems>()
                lineItems?.forEach {
                    newLineItems.add(it)
                    Log.d("cartDraftOrderInDetails:", "lineItemCurrentlyInCart: $it")
                }
                val lineItem = lineItems?.find { it.product_id == currentValue.first }
                if (currentValue.third == true) {
                    if (lineItem == null) {
                        Log.d("cartDraftOrderInDetails:", "AddingNewlineItem: $lineItem")

                        newLineItems.add(
                            LineItems(
                                variant_id = productFlow.value.let {
                                    if (it is ApiState.Success<*>) {
                                        (it.data as Product).variants[0].id
                                    } else {
                                        null
                                    }
                                },
                                quantity = currentValue.second ?: 1,
                                properties = listOf(
                                    LineItemProperty(
                                        name = "image_url",
                                        value = productFlow.value.let {
                                            if (it is ApiState.Success<*>) {
                                                (it.data as Product).image.src
                                            } else {
                                                null
                                            }
                                        }!!
                                    )
                                ),

                                )
                        )
                    } else {
                        //replace the old lineItem with the new one with the updated quantity
                        Log.d("cartDraftOrderInDetails:", "AddingOldlineItem: $lineItem")

                        newLineItems.remove(lineItem)
                        newLineItems.add(
                            LineItems(
                                currentValue.first!!,
                                currentValue.second?.toLong()
                            )
                        )
                    }
                } else {
                    if (lineItem != null) {
                        newLineItems.remove(lineItem)
                    }
                }

                Log.d("cartDraftOrderInDetails:", "newLineItems: $newLineItems")

                repoInterface.updateDraftOrder(
                    draftOrder.id!!,
                    DraftOrderResponse(
                        DraftOrder(
                            id = draftOrder.id,
                            line_items = newLineItems
                        )
                    )
                )

            }

        }
    }


    fun getSingleProduct(id: Long, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isNetworkAvailable(context)) {
                repoInterface.getSingleProduct(id).catch { e ->
                    _productFlow.emit(ApiState.Failure(e.message ?: ""))
                }.collect {
                    //change currency on product variants
                    it?.variants?.forEach { variant ->
                        variant.price = CurrencyUtils.convertCurrency(
                            variant.price.toDoubleOrNull(),
                            MyApplication.appInstance.settingsStore.currencyPreferencesFlow.first()
                        )
                    }
                    _productFlow.emit(ApiState.Success(it))

                }
                getCurrentCartDraftOrder()
            } else {
                _productFlow.emit(
                    ApiState.Failure(
                        "internet connection not found" +
                                ""
                    )
                )
            }
        }
    }

    private val _getDraftFlow: MutableStateFlow<ApiDraftLoginState> = MutableStateFlow(
        ApiDraftLoginState.Loading
    )
    val getDraftFlow: StateFlow<ApiDraftLoginState> = _getDraftFlow

    fun updateDraftOrder(draftOrderId: Long, draftResponse: DraftOrderResponse) {
        viewModelScope.launch {
            repoInterface.updateDraftOrder(draftOrderId, draftResponse)
        }
    }

    fun getDraftOrder(id: String) {
        viewModelScope.launch {
            val result =  iRepo.getDraftOrder(id)
            val inside = (result as ApiDraftLoginState.Success).data
            if (inside != null){
                _getDraftFlow.emit(ApiDraftLoginState.Success(result.data))
            }else{
                _getDraftFlow.emit(ApiDraftLoginState.Failure(Exception("NO DATA")))

            }

//            _getDraftFlow.value = result
        }
    }
}