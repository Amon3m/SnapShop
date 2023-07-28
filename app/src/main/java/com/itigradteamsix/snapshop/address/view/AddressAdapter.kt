package com.itigradteamsix.snapshop.address.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.itigradteamsix.snapshop.R
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val address = getItem(position)
        if (address.default == true) {

            holder.binding.mainAddress.text = address.address1
            holder.binding.subAddress.text = address.city
            holder.binding.phone.text = address.country
            holder.binding.imageView.setImageResource(R.drawable.baseline_home_24);
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
            holder.binding.imageView.setImageResource(R.drawable.baseline_homeblack_24);

            if(address.default==true) {
                holder.binding.defaultAddress.visibility = View.VISIBLE
            }
            else{
                holder.binding.defaultAddress.visibility = View.GONE

            }

            holder.binding.addressButton.setOnClickListener {
                val alertDialogBuilder = AlertDialog.Builder(context)
                alertDialogBuilder.setMessage("Are you sure you want to delete this address?")
                alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
                    listener.onAddressRemove(customerId, context, address.id!!)
                    notifyDataSetChanged()
                }

                alertDialogBuilder.setNegativeButton("No") { dialog, which ->
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.apply {
                    setIcon(R.drawable.baseline_info_24)
                    setTitle("Warning")
                }
                alertDialog.show()
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

