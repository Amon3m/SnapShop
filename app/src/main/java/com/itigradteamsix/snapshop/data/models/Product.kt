package com.itigradteamsix.snapshop.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val title: String,
    val bodyHtml: String,
    val vendor: String,
    val productType: String,
    val status: String
)

data class ProductCreationResponse(
    val product: Product
)

data class ProductListResponse(
    val products: List<Product>
)