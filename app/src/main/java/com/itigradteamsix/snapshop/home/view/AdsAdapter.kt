package com.itigradteamsix.snapshop.home.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.itigradteamsix.snapshop.databinding.ItemAdBinding
import com.itigradteamsix.snapshop.model.Coupon
import com.itigradteamsix.snapshop.model.copyToClipboard


class AdsAdapter : ListAdapter<Coupon, AdsAdapter.AdItemViewHolder>(
    AdsAdapter.AdsDiffUtilCallback()
) {

    companion object {
        const val TAG = "AdsAdapterAdapter"
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdsAdapter.AdItemViewHolder {
        val binding = ItemAdBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AdItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdsAdapter.AdItemViewHolder, position: Int) {
        val coupon = getItem(position)
        holder.bind(coupon)
    }

    inner class AdItemViewHolder(private val binding: ItemAdBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coupon: Coupon) {
            binding.imageViewCoupon.setImageResource(coupon.drawable)
            binding.root.setOnClickListener {
                copyToClipboard(coupon.code)
            }

        }
    }

    class AdsDiffUtilCallback : DiffUtil.ItemCallback<Coupon>() {
        override fun areItemsTheSame(oldItem: Coupon, newItem: Coupon): Boolean {
            return oldItem.drawable == newItem.drawable
        }

        override fun areContentsTheSame(oldItem: Coupon, newItem: Coupon): Boolean {
            return oldItem == newItem
        }
    }



}