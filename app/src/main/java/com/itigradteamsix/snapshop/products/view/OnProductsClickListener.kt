package com.itigradteamsix.snapshop.products.view

import com.itigradteamsix.snapshop.model.ProductsItem

interface OnProductsClickListener {
    fun onProductsClick(productID: Long)
    fun onWishClick(product: ProductsItem?)

}
