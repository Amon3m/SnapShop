package com.itigradteamsix.snapshop.model

//Request
data class MetafieldInput(
    val key: String,
    val value: String,
    val type: String,
    val namespace: String
)

data class MetaFieldCustomerRequest(
    val customer: MetaFieldCustomerInput
)

data class MetaFieldCustomerInput(
    val id: Long,
    val metafields: List<MetafieldInput>
)


//Response
data class MetaFieldResponse(
    val id : Long,
    val namespace: String,
    val key: String,
    val value: String,
    val description: String,
    val owner_id: Long,
)

data class MetaFieldListResponse(
    val metafields: List<MetaFieldResponse>
)