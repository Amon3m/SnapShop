package com.itigradteamsix.snapshop.model

import com.itigradteamsix.snapshop.favorite.model.AppliedDiscount
import com.itigradteamsix.snapshop.favorite.model.BillingAddress
import com.itigradteamsix.snapshop.favorite.model.ShippingAddress
import com.itigradteamsix.snapshop.favorite.model.ShippingLine
import com.itigradteamsix.snapshop.favorite.model.TaxLine


data class DraftOrderRequest(
    val draft_order: CreateDraftOrder? = null
)

//data class DraftOrderInput(
//    val name: String,
//    val line_items: List<LineItemInput>
//)

//data class LineItemInput(
//    val variant_id: Long,
//    val quantity: Int
//)

data class DraftOrderResponse(
    val draft_order: DraftOrder? = null
)

//data class DraftOrder(
//    val id: Long
//)



data class CreateDraftOrder(
    val id: Long? = null,
    val note: String? = null,
    val email: String? = null,
    val taxes_included: Boolean? = null,
    val currency: String? = null,
    val invoice_sent_at: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val tax_exempt: Boolean = true,  //HAMZA
    val completed_at: String? = null,
    val name: String? = null,
    val status: String? = null,
    val line_items: List<LineItem>? = null,
    val shipping_address: ShippingAddress? = null,
    val billing_address: BillingAddress? = null,
    val invoice_url: String? = null,
    val applied_discount: AppliedDiscount? = null,
    val order_id: String? = null,
    val shipping_line: ShippingLine? = null,
    val tax_lines: List<TaxLine>? = null,
    val tags: String? = null,
    val note_attributes: List<String>? = null,
    val total_price: String? = null,
    val subtotal_price: String? = null,
    val total_tax: String? = null,
    val payment_terms: String? = null,
    val admin_graphql_api_id: String? = null,
    val customer: Customer? = null
)

data class LineItem(
    val id: Long? = null,
    val variant_id: Long? = null,
    val product_id: Long? = null,
    val title: String? = null,
    val variant_title: String? = null,
    val sku: String? = null,
    val vendor: String? = null,
    val quantity: Int? = null,
    val requires_shipping: Boolean? = null,
    val taxable: Boolean? = null,
    val gift_card: Boolean? = null,
    val fulfillment_service: String? = null,
    val grams: Int? = null,
    val tax_lines: List<TaxLine>? = null,
    val applied_discount: AppliedDiscount? = null,
    val name: String? = null,
    val properties: List<Any>? = null,
    val custom: Boolean? = null,
    val price: String? = null,
    val admin_graphql_api_id: String? = null
)