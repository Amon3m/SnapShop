package com.itigradteamsix.snapshop.products.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.databinding.CategoriesItemBinding
import com.itigradteamsix.snapshop.model.ProductsItem

class ProductsAdapter(val context: Context, private val listener: OnProductsClickListener)
    : ListAdapter<ProductsItem?, ProductsViewHolder>(ProductsDiffUtil()) {
    lateinit var binding: CategoriesItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val inflater: LayoutInflater =parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= CategoriesItemBinding.inflate(inflater,parent,false)
        return ProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val currentObject =getItem(position)

        Glide.with(context)
            .load(currentObject?.image?.src)
            .placeholder(R.drawable.img_5)
            .error(R.drawable.img_6)
            .into(  holder.binding.productImageView)

        holder.binding.productTitle.text=currentObject?.title
        holder.binding.productPrice.text=currentObject?.variants?.get(0)?.price


//        holder.binding.fromDateTxt.text=currentObject.fromDate

        holder.binding.productCard.setOnClickListener {
            Log.d("productIDAdapter",currentObject?.id.toString())
            currentObject?.id?.let { it1 -> listener.onProductsClick(it1) }
        }
    }

}

class ProductsViewHolder(var binding: CategoriesItemBinding): RecyclerView.ViewHolder(binding.root)


class ProductsDiffUtil: DiffUtil.ItemCallback<ProductsItem?>(){
    override fun areItemsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
        return oldItem==newItem
    }


}