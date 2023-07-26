package com.itigradteamsix.snapshop.address.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.itigradteamsix.snapshop.data.models.Address
import com.itigradteamsix.snapshop.databinding.AddressItemBinding

class AddressAdapter (var context: Context,  var listener :OnDeleteListener,  var customerId :Long) : ListAdapter<Address, AddressAdapter.ViewHolder>(
    AddressAdapter.DiffUtils
) {
    class ViewHolder(val binding: AddressItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val address = getItem(position)
        if (address.default == true) {

            holder.binding.mainAddress.text = address.address1
            holder.binding.subAddress.text = address.city
            holder.binding.phone.text = address.country
            if(address.default==true) {
                holder.binding.defaultAddress.visibility = View.VISIBLE
            }
            else{
                holder.binding.defaultAddress.visibility = View.GONE

            }
            holder.binding.addressButton.setOnClickListener {
                Toast.makeText(context,"You can't remove default address",Toast.LENGTH_SHORT).show()
            }
            holder.binding.imageView.setOnClickListener {
                Toast.makeText(context,"This address is already your default",Toast.LENGTH_SHORT).show()
            }

        } else {
            holder.binding.mainAddress.text = address.address1
            holder.binding.subAddress.text = address.city
            holder.binding.phone.text = address.country
            if(address.default==true) {
                holder.binding.defaultAddress.visibility = View.VISIBLE
            }
            else{
                holder.binding.defaultAddress.visibility = View.GONE

            }

            holder.binding.addressButton.setOnClickListener {
                listener.onAddressRemove(customerId, context, address.id!!)
            }
            holder.binding.imageView.setOnClickListener {
                listener.onDefaultAddress(customerId,context, address.id!!)
                holder.binding.defaultAddress.visibility = View.VISIBLE
                Toast.makeText(context,"This address is now your default",Toast.LENGTH_SHORT).show()
            }


        }
    }
        object DiffUtils : DiffUtil.ItemCallback<Address>() {
            override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
                return oldItem == newItem
            }

        }
    }

