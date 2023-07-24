package com.itigradteamsix.snapshop.shoppingcart.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.itigradteamsix.snapshop.databinding.ShoppingCartItemBinding
import com.itigradteamsix.snapshop.favorite.model.LineItems
import com.itigradteamsix.snapshop.model.LineItem

class ShoppingCartAdapter(
    private val onIncreaseClickListener: (LineItems) -> Unit,
    private val onDecreaseClickListener: (LineItems) -> Unit,
    private val onDeleteClickListener: (LineItems) -> Unit,
    private val onItemClicked: (LineItems) -> Unit
) : ListAdapter<LineItems, ShoppingCartAdapter.ShopingCartViewHolder>(ShopingCartDiffCallback()) {

    companion object {
        const val TAG = "ShoppingCartAdapter"
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoppingCartAdapter.ShopingCartViewHolder {
        val binding = ShoppingCartItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShopingCartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingCartAdapter.ShopingCartViewHolder, position: Int) {
        val lineItem = getItem(position)
        holder.bind(lineItem)
    }

    inner class ShopingCartViewHolder(private val binding: ShoppingCartItemBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        fun bind(lineItem: LineItems) {
            binding.shoppingCartItemNameTextview.text = lineItem.title
            binding.shoppingCartItemPriceTextview.text = lineItem.price.toString()
            binding.shoppingCartItemCountTextview.text = lineItem.quantity.toString()
            // line item properties with the name "image_url"
            val productImage  = lineItem.properties?.find { it.name == "image_url" }
            if (productImage != null) {
                Glide.with(binding.root).load(productImage.value).into(binding.shoppingCartItemImage)
            } else {
                Log.d(TAG, "productImage is null")
            }






            binding.shoppingCartItemPlusButton.setOnClickListener {
                onIncreaseClickListener(lineItem)
            }
            binding.shoppingCartItemMinusButton.setOnClickListener {
                onDecreaseClickListener(lineItem)
            }
            binding.shoppingCartItemDeleteButton.setOnClickListener {
                onDeleteClickListener(lineItem)
            }
            binding.root.setOnClickListener {
                onItemClicked(lineItem)
            }
        }
    }



    class ShopingCartDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback<LineItems>() {
        override fun areItemsTheSame(oldItem: LineItems, newItem: LineItems): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LineItems, newItem: LineItems): Boolean {
            return oldItem == newItem
        }
    }


}