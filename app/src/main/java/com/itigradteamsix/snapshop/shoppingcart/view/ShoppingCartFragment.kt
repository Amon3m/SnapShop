package com.itigradteamsix.snapshop.shoppingcart.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentShoppingCartBinding
import com.itigradteamsix.snapshop.model.ProductListResponse
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.model.SmartCollectionResponse
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState

import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModel
import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModelFactory
import kotlinx.coroutines.launch


class ShoppingCartFragment : Fragment() {

lateinit var binding: FragmentShoppingCartBinding
lateinit var shoppingCartViewModel: ShoppingCartViewModel
lateinit var shoppingCartviewModelFactory: ShoppingCartViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shoppingCartviewModelFactory = ShoppingCartViewModelFactory(
            Repository.getInstance(
                ApiClient,
                ConcreteLocalSource(requireContext())
            )
        )
        shoppingCartViewModel = ViewModelProvider(this, shoppingCartviewModelFactory).get(ShoppingCartViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        shoppingCartViewModel.getAllProducts(requireContext())

         shoppingCartViewModel.getSmartCollections(requireContext())

        // Inflate the layout for this fragment
        binding= FragmentShoppingCartBinding.inflate(inflater,container,false)
        return binding.root}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            shoppingCartViewModel.productList.collect {
                when (it) {
                    is ApiState.Success<*> -> {

                        val data = it.data as? ProductListResponse


                        val product=data?.products
                        Log.e("Success ******","${product?.get(0)?.title}")
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()

                    }
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()

                        Log.e("Failure ******","Failure")

                    }

                    else -> {

                        Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

//        lifecycleScope.launch {
//            viewModel.productList.collect {
//                it.forEach { product ->
//                    Log.d("ShoppingCartFragment_ALLPRODUCTS", " ${product.title}")
//                }
//            }
//        }
        lifecycleScope.launch {
            shoppingCartViewModel.smartCollection.collect {
                when (it) {
                    is ApiState.Success<*> -> {
                        val data = it.data as? SmartCollectionResponse


                        Log.e("Success xxxxxxx","${data?.smart_collection?.title}" )
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()

                    }
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    }

                    else -> {

                        Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
//        lifecycleScope.launch {
//            viewModel.smartCollection.collect { smartCollection ->
//                if (smartCollection != null){
//                    Log.d("ShoppingCartFragment_SMARTCOLLECTION", " ${smartCollection.toString()}")
//                    Toast.makeText(requireContext(), "GOT COLLECTION ${smartCollection.title} ", Toast.LENGTH_SHORT).show()
//                }
//
//            }
//        }

    }

}