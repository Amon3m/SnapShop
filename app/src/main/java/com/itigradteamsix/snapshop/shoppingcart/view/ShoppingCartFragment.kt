package com.itigradteamsix.snapshop.shoppingcart.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.data.repository.ShopRepository
import com.itigradteamsix.snapshop.data.repository.local.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentShoppingCartBinding
import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ShoppingCartFragment : Fragment() {

lateinit var binding: FragmentShoppingCartBinding
lateinit var viewModel: ShoppingCartViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentShoppingCartBinding.inflate(inflater,container,false)
        return binding.root}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ShoppingCartViewModel(ShopRepository.getInstance(ConcreteLocalSource.getInstance(requireContext())))
        viewModel.getAllProducts()
        viewModel.getSmartCollections()

        lifecycleScope.launch {
            viewModel.productList.collect {
                it.forEach { product ->
                    Log.d("ShoppingCartFragment_ALLPRODUCTS", " ${product.title}")
                }
            }
        }

        lifecycleScope.launch {
            viewModel.smartCollection.collect { smartCollection ->
                if (smartCollection != null){
                    Log.d("ShoppingCartFragment_SMARTCOLLECTION", " ${smartCollection.toString()}")
                    Toast.makeText(requireContext(), "GOT COLLECTION ${smartCollection.title} ", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

}