package com.itigradteamsix.snapshop.brands.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.databinding.BrandItemBinding
import com.itigradteamsix.snapshop.model.SmartCollectionsItem
import com.itigradteamsix.snapshop.model.SmartCollectionsResponse

class BrandsAdapter(val context: Context, private val listener: OnBrandsClickListener)
    :ListAdapter<SmartCollectionsItem?, BrandsViewHolder>(BrandsDiffUtil()) {
    lateinit var binding: BrandItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandsViewHolder {
        val inflater: LayoutInflater =parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding= BrandItemBinding.inflate(inflater,parent,false)
        return BrandsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandsViewHolder, position: Int) {
        val currentObject =getItem(position)

        Glide.with(context)
            .load(currentObject?.image?.src)
            .placeholder(R.drawable.img_5)
            .error(R.drawable.img_6)
            .into(  holder.binding.brand1ImageView)

        holder.binding.productTitle2.text=currentObject?.title


        holder.binding.brand1Card.setOnClickListener {
            listener.onBrandClick(currentObject)
        }
    }

}

class BrandsViewHolder(var binding: BrandItemBinding): RecyclerView.ViewHolder(binding.root)


class BrandsDiffUtil: DiffUtil.ItemCallback<SmartCollectionsItem?>(){
    override fun areItemsTheSame(oldItem: SmartCollectionsItem, newItem: SmartCollectionsItem): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: SmartCollectionsItem, newItem: SmartCollectionsItem): Boolean {
        return oldItem==newItem
    }

}