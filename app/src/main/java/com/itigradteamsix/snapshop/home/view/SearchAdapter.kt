package com.itigradteamsix.snapshop.home.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.itigradteamsix.snapshop.databinding.SearchRowBinding
import com.itigradteamsix.snapshop.model.Product
import androidx.navigation.findNavController

class SearchAdapter (var context: Context) : ListAdapter<Product, SearchAdapter.ViewHolder>(
    DiffUtils
) {
    class ViewHolder(val binding: SearchRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)
        holder.binding.txtViewProductName.text  = product.title
        holder.binding.root.setOnClickListener{

            val action = HomeFragmentDirections.actionHomeFragmentToProductInfoFragment(product.id)
            holder.binding.root.findNavController().navigate(action)         }
    }
    object DiffUtils : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
}