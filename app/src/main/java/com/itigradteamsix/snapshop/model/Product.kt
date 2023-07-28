package com.itigradteamsix.snapshop.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Product(
    @PrimaryKey(autoGenerate = false)
    var id: Long = 1,
    var title: String = "",
    var body_html: String = "",
    var vendor: String = "",
    var product_type: String = "",
    var created_at: String = "",
    var handle: String = "",
    var updated_at: String = "",
    var published_at: String = "",
    var template_suffix: String? = null,
    var status: String = "",
    var published_scope: String = "",
    var tags: String = "",
    var admin_graphql_api_id: String = "",
    var variants: List<ProductVariant> = emptyList(),
    var options: List<ProductOption> = emptyList(),
    var images: List<ProductImage> = emptyList(),
    var image: ProductImage = ProductImage()
)




data class ProductCreationResponse(
    val product: Product
)

data class ProductListResponse(
    val products: List<Product>
)

data class ProductResponse(
    var product: Product
)



data class ProductVariant(
    var id: Long = 0,
    var product_id: Long = 0,
    var title: String = "",
    var price: String = "",
    var sku: String = "",
    var position: Int = 0,
    var inventory_policy: String = "",
    var compare_at_price: String? = null,
    var fulfillment_service: String = "",
    var inventory_management: String = "",
    var option1: String = "",
    var option2: String = "",
    var option3: String? = null,
    var created_at: String = "",
    var updated_at: String = "",
    var taxable: Boolean = false,
    var barcode: String? = null,
    var grams: Int = 0,
    var image_id: String? = null,
    var weight: Double = 0.0,
    var weight_unit: String = "kg",
    var inventory_item_id: Long = 0,
    var inventory_quantity: Int = 0,
    var old_inventory_quantity: Int = 0,
    var requires_shipping: Boolean = false,
    var admin_graphql_api_id: String = ""
)

data class ProductOption(
    var id: Long = 0,
    var product_id: Long = 0,
    var name: String = "",
    var position: Int = 0,
    var values: List<String> = emptyList()
)

data class ProductImage(
    var id: Long = 0,
    var product_id: Long = 0,
    var position: Int = 0,
    var created_at: String = "",
    var updated_at: String = "",
    var alt: String? = null,
    var width: Int = 0,
    var height: Int = 0,
    var src: String = "",
    var variant_ids: List<Long> = emptyList(),
    var admin_graphql_api_id: String = ""
)
