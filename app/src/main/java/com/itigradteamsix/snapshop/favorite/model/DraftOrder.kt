package com.itigradteamsix.snapshop.favorite.model

import com.itigradteamsix.snapshop.model.Customer

data class DraftOrderResponse(
    var draft_order: DraftOrder? = null
)

data class DraftOrder(
    val id: Long? = null,
    val note: String? = null,
    val email: String? = null,
    val taxes_included: Boolean? = null,
    val currency: String? = null,
    val invoice_sent_at: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null,
    val tax_exempt: Boolean? = null,
    val completed_at: String? = null,
    val name: String? = null,
    val status: String? = null,
    var line_items: List<LineItems>? = null,
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

data class LineItems(
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

data class ShippingAddress(
    val first_name: String? = null,
    val address1: String? = null,
    val phone: String? = null,
    val city: String? = null,
    val zip: String? = null,
    val province: String? = null,
    val country: String? = null,
    val last_name: String? = null,
    val address2: String? = null,
    val company: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val name: String? = null,
    val country_code: String? = null,
    val province_code: String? = null
)

data class BillingAddress(
    val first_name: String? = null,
    val address1: String? = null,
    val phone: String? = null,
    val city: String? = null,
    val zip: String? = null,
    val province: String? = null,
    val country: String? = null,
    val last_name: String? = null,
    val address2: String? = null,
    val company: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val name: String? = null,
    val country_code: String? = null,
    val province_code: String? = null
)

data class AppliedDiscount(
    val description: String? = null,
    val value: String? = null,
    val title: String? = null,
    val amount: String? = null,
    val value_type: String? = null
)

data class ShippingLine(
    val title: String? = null,
    val custom: Boolean? = null,
    val handle: String? = null,
    val price: String? = null
)

data class TaxLine(
    val title: String? = null,
    val price: String? = null
)
