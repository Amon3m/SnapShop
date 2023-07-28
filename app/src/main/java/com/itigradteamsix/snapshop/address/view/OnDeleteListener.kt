package com.itigradteamsix.snapshop.address.view

import android.content.Context

interface OnDeleteListener {
    fun onAddressRemove(customerId: Long, context :Context,addressId :Long)
    fun onDefaultAddress(customerId: Long, context :Context,addressId :Long)

}