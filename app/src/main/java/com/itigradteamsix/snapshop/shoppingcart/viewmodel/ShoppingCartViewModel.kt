package com.itigradteamsix.snapshop.shoppingcart.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.favorite.model.LineItems
import com.itigradteamsix.snapshop.model.RepoInterface
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.settings.currency.CurrencyUtils.convertCurrency
import com.itigradteamsix.snapshop.settings.currency.CurrencyUtils.convertCurrencyWithoutSymbol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ShoppingCartViewModel(private val repoInterface: RepoInterface) : ViewModel() {

    companion object {
        const val TAG = "ShoppingCartViewModel"
    }

    //state flow for cart draft order
    private val _cartDraftOrder = MutableStateFlow<ApiState>(ApiState.Loading)
    val cartDraftOrder: StateFlow<ApiState>
        get() = _cartDraftOrder

    private val _orderCompleteState = MutableStateFlow<Boolean>(false)
    val orderCompleteState: StateFlow<Boolean>
        get() = _orderCompleteState

    private val _discountPercenatge = MutableStateFlow<Int>(0)
    val discountPercentage: StateFlow<Int>
        get() = _discountPercenatge


    fun changeDiscountPercentage(discountPercentage: Int) {
        _discountPercenatge.value = discountPercentage
    }


    fun getCartDraftOrder() {

        viewModelScope.launch {
            //get the current cart draft order id from dataStore
            val currencyPreferences =
                MyApplication.appInstance.settingsStore.currencyPreferencesFlow.first()


            val cartDraftOrderId =
                MyApplication.appInstance.settingsStore.userPreferencesFlow.first().cartDraftOrderId
            Log.d(TAG, "getCartDraftOrder: Cart Draft order ID $cartDraftOrderId")
            val cartDraftOrder = ApiClient.getDraftOrderAsFlow(cartDraftOrderId)
            cartDraftOrder?.collectLatest {
                Log.d(TAG, "getCartDraftOrder: ${it.line_items}")

                if (!it.line_items.isNullOrEmpty()) {

                    if (it.line_items?.size == 1 && it.line_items?.get(0)?.title == "empty") {
                        _cartDraftOrder.emit(ApiState.Failure("empty cart"))
                        Log.d(TAG, "empty cart only one item named empty")
                    } else {
                        //convert price on total price and line items
                        it.total_price = it.total_price?.let { it1 ->
                            convertCurrencyWithoutSymbol(
                                it1,
                                currencyPreferences
                            )
                        }

                        it.line_items?.forEach { lineItem ->
                            lineItem.price = lineItem.price?.let { it1 ->
                                convertCurrencyWithoutSymbol(
                                    it1,
                                    currencyPreferences
                                )
                            }
                        }

                        _cartDraftOrder.emit(ApiState.Success(it))
                    }
                } else {
                    _cartDraftOrder.emit(ApiState.Failure("DraftOrder is null"))
                }
            }

        }

    }

    fun increaseQuantity(lineItem: LineItems) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("cartDraftOrderInDetails:", "increaseQuantityOf: $lineItem")
            val draftOrder = (cartDraftOrder.value as ApiState.Success<*>).data as DraftOrder
            val lineItems = draftOrder.line_items

            val newLineItems = lineItems?.map {
                if (it.id == lineItem.id) {
                    it.quantity = lineItem.quantity?.plus(1)
                }
                it
            }
            val newOrder = draftOrder.copy(line_items = newLineItems)

            val resultOrder = ApiClient.updateDraftOrderWithNewItems(
                draftOrder.id!!,
                DraftOrderResponse(newOrder)
            )
//            _cartDraftOrder.emit(ApiState.Success(resultOrder))

            getCartDraftOrder()


        }
    }

    fun decreaseQuantity(lineItem: LineItems) {
        viewModelScope.launch(Dispatchers.IO) {
            //decrease the quantity of the lineItem by 1 and if it's 0, delete it from the cart
            Log.d("cartDraftOrderInDetails:", "decreaseQuantityOf: $lineItem")
            val draftOrder = (cartDraftOrder.value as ApiState.Success<*>).data as DraftOrder
            val lineItems = draftOrder.line_items
            //if the quantity is 1, delete the lineItem from the cart instead of decreasing the quantity by 1
            if (lineItem.quantity == 1) {
                deleteLineItem(lineItem)
            } else {
                val newLineItems = lineItems?.map {
                    if (it.id == lineItem.id) {
                        it.quantity = lineItem.quantity?.minus(1)
                    }
                    it
                }
                val newOrder = draftOrder.copy(line_items = newLineItems)

                val resultOrder = ApiClient.updateDraftOrderWithNewItems(
                    draftOrder.id!!,
                    DraftOrderResponse(newOrder)
                )
//                _cartDraftOrder.emit(ApiState.Success(resultOrder))
                getCartDraftOrder()


            }

        }

    }

    fun deleteLineItem(lineItem: LineItems) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("cartDraftOrderInDetails:", "deleteLineItem: $lineItem")
            val draftOrder = (cartDraftOrder.value as ApiState.Success<*>).data as DraftOrder
            val lineItems = draftOrder.line_items
            var newLineItems = lineItems?.filter { it.id != lineItem.id }
            //if newLineItems is empty, add custom line item with quantity 1 and title "empty" and price 1
            if (newLineItems.isNullOrEmpty()) {
                newLineItems = listOf(LineItems(title = "empty", quantity = 1, price = "1"))
            }

            val newOrder = draftOrder.copy(line_items = newLineItems)


            val resultOrder = ApiClient.updateDraftOrderWithNewItems(
                draftOrder.id!!,
                DraftOrderResponse(newOrder)
            )
            Log.d("cartDraftOrderInDetails:", "resultOrderLineItems: ${resultOrder?.line_items}")
            if (!resultOrder?.line_items.isNullOrEmpty()) {
//                _cartDraftOrder.emit(ApiState.Success(resultOrder))
                getCartDraftOrder()

            } else {
                _cartDraftOrder.emit(ApiState.Failure("DraftOrder is null"))
            }

        }
    }

    fun completeDraftOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            _orderCompleteState.emit(true)

            val draftOrder = (cartDraftOrder.value as ApiState.Success<*>).data as DraftOrder
            val completion = ApiClient.completeDraftOrder(draftOrder.id!!)
//            Log.d(TAG, "completeDraftOrder: $orderCompletion")

        }
    }


}