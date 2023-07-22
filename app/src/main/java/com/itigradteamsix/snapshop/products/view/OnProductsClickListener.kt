package com.itigradteamsix.snapshop.products.view

import com.itigradteamsix.snapshop.model.ProductsItem

interface OnProductsClickListener {
    fun onProductsClick(product: ProductsItem?)
    fun onWishClick(product: ProductsItem?)

}
