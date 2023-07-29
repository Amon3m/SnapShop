package com.itigradteamsix.snapshop.settings.currency

data class ApiResponse(
    val amount: String,
    val base_currency_code: String,
    val base_currency_name: String,
    var rates: HashMap<String, Rates> = HashMap(),
    val status: String,
    val updated_date: String
)

data class Rates(
    val currency_name: String,
    val rate: String,
    val rate_for_amount: Double
)