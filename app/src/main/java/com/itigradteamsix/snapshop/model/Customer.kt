package com.itigradteamsix.snapshop.model

import com.itigradteamsix.snapshop.data.models.Address
import com.itigradteamsix.snapshop.data.models.EmailMarketingConsent
import com.itigradteamsix.snapshop.data.models.SmsMarketingConsent

data class Customer(
   var id: Long?=null,
   var email: String?=null,
   val email_marketing_consent: EmailMarketingConsent?=null,
   var accepts_marketing: Boolean?=null,
   var created_at: String?=null,
   var updated_at: String?=null,
   var first_name: String?=null,
   var last_name: String?=null,
   var orders_count: Int?=null,
   var state: String?=null,
   var total_spent: String?=null,
   var last_order_id: Long?=null,
   var note: String?=null,
   var verified_email: Boolean?=null,
   var multipass_identifier: String?=null,
   var tax_exempt: Boolean?=null,
   var phone: String?=null,
   var tags: String?=null,
   var last_order_name: String?=null,
   var  currency: String?=null,
   var addresses: List<Address>?=null,
   var accepts_marketing_updated_at: String?=null,
   val sms_marketing_consent: SmsMarketingConsent?=null,
   var marketing_opt_in_level: Any?=null,
   var tax_exemptions: List<Any>?=null,
   var admin_graphql_api_id: String?=null,
   var default_address: Address?=null
)
