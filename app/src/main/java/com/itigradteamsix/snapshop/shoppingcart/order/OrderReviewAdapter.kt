package com.itigradteamsix.snapshop.shoppingcart.order

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.itigradteamsix.snapshop.databinding.OrderReviewItemBinding
import com.itigradteamsix.snapshop.databinding.ShoppingCartItemBinding
import com.itigradteamsix.snapshop.favorite.model.LineItems
import com.itigradteamsix.snapshop.shoppingcart.view.ShoppingCartAdapter

class OrderReviewAdapter : ListAdapter<LineItems, OrderReviewAdapter.OrederItemViewHolder>(
    ShoppingCartAdapter.ShopingCartDiffCallback()
) {

    companion object {
        const val TAG = "OrderReviewAdapter"
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderReviewAdapter.OrederItemViewHolder {
        val binding = OrderReviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrederItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderReviewAdapter.OrederItemViewHolder, position: Int) {
        val lineItem = getItem(position)
        holder.bind(lineItem)
    }

    inner class OrederItemViewHolder(private val binding: OrderReviewItemBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        fun bind(lineItem: LineItems) {
            val productImage  = lineItem.properties?.find { it.name == "image_url" }
            if (productImage != null) {
                Glide.with(binding.root).load(productImage.value).into(binding.imageView)
            } else {
                Log.d(TAG, "productImage is null")
            }
        }
    }


}