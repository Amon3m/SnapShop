package com.itigradteamsix.snapshop.model

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.R

data class Coupon(
    //first is the ad drawable
    val drawable: Int,
    val code: String,
    val discountPercentage: Int = 0
){
    companion object {
        val coupons = listOf(
            Coupon(R.drawable.ad1, "SUMMER20", 20),
            Coupon(R.drawable.ad2, "WEEKEND30", 30),
            Coupon(R.drawable.ad3, "FLASH25", 25),
        )
    }
}




fun copyToClipboard(couponCode: String) {
    val clipboard = MyApplication.appContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Coupon Code", couponCode)
    clipboard.setPrimaryClip(clip)

    Toast.makeText(MyApplication.appContext, "Coupon code copied to clipboard", Toast.LENGTH_SHORT).show()
}