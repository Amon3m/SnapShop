package com.itigradteamsix.snapshop.products.view

interface FilterOptionsListener {
    fun onFilterOptionsSelected(minPrice: Int, maxPrice: Int, type: String?
                                ,isFilter:Boolean,isPrice:Boolean,isType:Boolean)
}