package com.itigradteamsix.snapshop.model

data class Discount(
    val description: String,
    val value_type: String,
    val value: String,
    val amount: String,
    val title: String
)

data class DiscountDraftOrderRequest(
    val draft_order: DiscountDraftOrderInput
)

data class DiscountDraftOrderInput(
    val id: Long,
    val applied_discount: Discount
)

data class DiscountDraftOrderResponse(
    val draft_order: DiscountDraftOrderData
)

data class DiscountDraftOrderData(
    val id: Long,
)