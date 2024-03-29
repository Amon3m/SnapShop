package com.itigradteamsix.snapshop.favorite.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.databinding.WishlistRowBinding
import com.itigradteamsix.snapshop.favorite.model.FavoritePojo

class FavoriteAdapter (private var favoriteList: List<FavoritePojo>, val context: Context , val onFavClickListener: OnFavClickListener) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private lateinit var binding: WishlistRowBinding

    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(values: List<FavoritePojo?>?) {
        this.favoriteList=ArrayList()
        this.favoriteList = values as List<FavoritePojo>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = WishlistRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct = favoriteList[position+1]

        holder.binding.txtViewReviewerName.text = currentProduct.title
        //add discount feature later
        holder.binding.priceAfterDiscount.text = currentProduct.price.plus(" $")
        holder.binding.colorTv.text = currentProduct.color
        Glide.with(context)
            .load(currentProduct.imageSrc)
            .into(holder.binding.imgViewProduct)
        holder.binding.root.setOnClickListener {
            onFavClickListener.onFavClickListener(currentProduct.productId!!)

        }
        holder.binding.delete.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setMessage("Are you sure you want to delete this item?")
            alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
                onFavClickListener.onDeleteClickListener(currentProduct.productId!!)
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
    }

    override fun getItemCount() = favoriteList.size-1

    inner class ViewHolder(var binding: WishlistRowBinding) : RecyclerView.ViewHolder(binding.root)
}