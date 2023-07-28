package com.itigradteamsix.snapshop.model

import com.google.gson.annotations.SerializedName

data class CreateOrderResponse(

	@field:SerializedName("draft_order")
	val draftOrder: DraftOrder? = null
)

data class CreateCustomer(

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("last_order_name")
	val lastOrderName: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("multipass_identifier")
	val multipassIdentifier: Any? = null,

	@field:SerializedName("accepts_marketing_updated_at")
	val acceptsMarketingUpdatedAt: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("accepts_marketing")
	val acceptsMarketing: Boolean? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("marketing_opt_in_level")
	val marketingOptInLevel: Any? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("total_spent")
	val totalSpent: String? = null,

	@field:SerializedName("last_order_id")
	val lastOrderId: Long? = null,

	@field:SerializedName("tax_exempt")
	val taxExempt: Boolean= true,

	@field:SerializedName("email_marketing_consent")
	val emailMarketingConsent: EmailMarketingConsent? = null,

	@field:SerializedName("last_name")
	val lastName: Any? = null,

	@field:SerializedName("verified_email")
	val verifiedEmail: Boolean? = null,

	@field:SerializedName("tags")
	val tags: String? = null,

	@field:SerializedName("orders_count")
	val ordersCount: Int? = null,

	@field:SerializedName("sms_marketing_consent")
	val smsMarketingConsent: Any? = null,

	@field:SerializedName("phone")
	val phone: Any? = null,

	@field:SerializedName("admin_graphql_api_id")
	val adminGraphqlApiId: String? = null,

	@field:SerializedName("tax_exemptions")
	val taxExemptions: List<Any?>? = null
)

data class TaxLinesItem(

	@field:SerializedName("rate")
	val rate: Any? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class AppliedDiscount(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("value_type")
	val valueType: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("value")
	val value: String? = null
)

data class DraftOrder(

	@field:SerializedName("note")
	val note: Any? = null,

	@field:SerializedName("applied_discount")
	val appliedDiscount: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("billing_address")
	val billingAddress: Any? = null,

	@field:SerializedName("taxes_included")
	val taxesIncluded: Boolean? = null,

	@field:SerializedName("line_items")
	val lineItems: List<LineItemsItem?>? = null,

	@field:SerializedName("payment_terms")
	val paymentTerms: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("tax_lines")
	val taxLines: List<TaxLinesItem?>? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("shipping_address")
	val shippingAddress: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("subtotal_price")
	val subtotalPrice: String? = null,

	@field:SerializedName("total_price")
	val totalPrice: String? = null,

	@field:SerializedName("tax_exempt")
	val taxExempt: Boolean = true,

	@field:SerializedName("invoice_sent_at")
	val invoiceSentAt: Any? = null,

	@field:SerializedName("total_tax")
	val totalTax: String? = null,

	@field:SerializedName("tags")
	val tags: String? = null,

	@field:SerializedName("completed_at")
	val completedAt: String? = null,

	@field:SerializedName("note_attributes")
	val noteAttributes: List<Any?>? = null,

	@field:SerializedName("admin_graphql_api_id")
	val adminGraphqlApiId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("shipping_line")
	val shippingLine: Any? = null,

	@field:SerializedName("order_id")
	val orderId: Long? = null,

	@field:SerializedName("invoice_url")
	val invoiceUrl: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("customer")
	val customer: CreateCustomer? = null
)

data class EmailMarketingConsent(

	@field:SerializedName("consent_updated_at")
	val consentUpdatedAt: Any? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("opt_in_level")
	val optInLevel: String? = null
)

data class LineItemsItem(

	@field:SerializedName("variant_title")
	val variantTitle: String? = null,

	@field:SerializedName("quantity")
	val quantity: Int? = null,

	@field:SerializedName("taxable")
	val taxable: Boolean? = null,

	@field:SerializedName("gift_card")
	val giftCard: Boolean? = null,

	@field:SerializedName("fulfillment_service")
	val fulfillmentService: String? = null,

	@field:SerializedName("applied_discount")
	val appliedDiscount: Any? = null,

	@field:SerializedName("requires_shipping")
	val requiresShipping: Boolean? = null,

	@field:SerializedName("custom")
	val custom: Boolean? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("variant_id")
	val variantId: Long? = null,

	@field:SerializedName("tax_lines")
	val taxLines: List<TaxLinesItem?>? = null,

	@field:SerializedName("vendor")
	val vendor: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("product_id")
	val productId: Long? = null,

	@field:SerializedName("admin_graphql_api_id")
	val adminGraphqlApiId: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Long? = null,

	@field:SerializedName("sku")
	val sku: String? = null,

	@field:SerializedName("grams")
	val grams: Int? = null,

	@field:SerializedName("properties")
	val properties: List<Any?>? = null
)
