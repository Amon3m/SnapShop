package com.itigradteamsix.snapshop.model

data class SmartCollection(
    val id: Long,
    val handle: String,
    val title: String,
    val updatedAt: String,
    val bodyHtml: String,
    val publishedAt: String,
    val sortOrder: String,
    val templateSuffix: String?,
    val productsCount: Int,
    val disjunctive: Boolean,
    val rules: List<CollectionRule>,
    val publishedScope: String,
    val adminGraphqlApiId: String,
    val image: CollectionImage
)

data class CollectionRule(
    val column: String,
    val relation: String,
    val condition: String
)

data class CollectionImage(
    val createdAt: String,
    val alt: String,
    val width: Int,
    val height: Int,
    val src: String
)


data class SmartCollectionsResponseH(
    val smart_collections: List<SmartCollection>
)

data class SmartCollectionResponse(
    val smart_collection: SmartCollection
)

