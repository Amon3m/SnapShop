package com.itigradteamsix.snapshop.orders.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.brands.view.OnBrandsClickListener
import com.itigradteamsix.snapshop.databinding.BrandItemBinding
import com.itigradteamsix.snapshop.databinding.OrdersItemBinding
import com.itigradteamsix.snapshop.model.OrdersItem
import com.itigradteamsix.snapshop.model.SmartCollectionsItem

class OrdersAdapter (val context: Context)
    : ListAdapter<OrdersItem?, OrdersViewHolder>(OrdersDiffUtil()) {
    lateinit var binding: OrdersItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val inflater: LayoutInflater =parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= OrdersItemBinding.inflate(inflater,parent,false)
        return OrdersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val currentObject =getItem(position)

//        Glide.with(context)
//            .load(currentObject?.image?.src)
//            .placeholder(R.drawable.img_5)
//            .error(R.drawable.img_6)
//            .into(  holder.binding.prorductImageView)

        holder.binding.titleProduct2.text=currentObject?.id.toString()
        holder.binding.createdText.text=currentObject?.createdAt
        holder.binding.priceOrder.text=currentObject?.currentTotalPrice
        holder.binding.priceCurr.text=currentObject?.currency





//        holder.binding.brand1Card.setOnClickListener {
//            listener.onBrandClick(currentObject)
//        }
    }

}

class OrdersViewHolder(var binding: OrdersItemBinding): RecyclerView.ViewHolder(binding.root)


class OrdersDiffUtil: DiffUtil.ItemCallback<OrdersItem?>(){
    override fun areItemsTheSame(oldItem: OrdersItem, newItem: OrdersItem): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: OrdersItem, newItem: OrdersItem): Boolean {
        return oldItem==newItem
    }

}