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





data class UpdateMetafieldRequest(
    val metafield: UpdateMetafieldInput
)

data class UpdateMetafieldInput(
    val id: Long,
    val value: String,
    val type: String
)

data class UpdateMetafieldResponse(
    val metafield: UpdateMetafieldData
)

data class UpdateMetafieldData(
    val value: String,
    val owner_id: Long,
    val namespace: String,
    val key: String,
    val id: Long,
    val description: String,
    val created_at: String,
    val updated_at: String,
    val owner_resource: String,
    val type: String,
    val admin_graphql_api_id: String
)