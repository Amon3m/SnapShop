package com.itigradteamsix.snapshop.data.models


data class Address(
    var id: Long?=null,
    var customer_id: Long?=null,
    var first_name: String?=null,
    var last_name: String?=null,
    var company: String?=null,
    var address1: String?=null,
    var address2: String?=null,
    var city: String?=null,
    var province: String?=null,
    var country: String?=null,
    var zip: String?=null,
    var phone: String?=null,
    var name: String?=null,
    var province_code: String?=null,
    var country_code: String?=null,
    var country_name: String?=null,
    var default: Boolean?=null
)
