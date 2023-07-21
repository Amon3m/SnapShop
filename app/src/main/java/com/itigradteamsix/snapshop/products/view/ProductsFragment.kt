package com.itigradteamsix.snapshop.products.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.itigradteamsix.snapshop.products.viewmodel.ProductViewModel
import com.itigradteamsix.snapshop.products.viewmodel.ProductViewModelFactory
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentProductsBinding

import com.itigradteamsix.snapshop.model.ListProductsResponse
import com.itigradteamsix.snapshop.model.ProductsItem
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.launch


class ProductsFragment : Fragment(), OnProductsClickListener{
lateinit var binding: FragmentProductsBinding
lateinit var productsAdapter: ProductsAdapter
lateinit var productViewModel:ProductViewModel
lateinit var productViewModelFactory: ProductViewModelFactory
var collectionId:Long=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModelFactory = ProductViewModelFactory(
            Repository.getInstance(
                ApiClient,
                ConcreteLocalSource(requireContext())
            )
        )

        productViewModel = ViewModelProvider(this, productViewModelFactory).get(ProductViewModel::class.java)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

       binding= FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectionId = ProductsFragmentArgs.fromBundle(requireArguments()).collectionId
        Log.e("collectionId","$collectionId")
        productViewModel.getProducts(requireContext(),collectionId)
        productsAdapter = ProductsAdapter(requireContext(),this)

        binding.productRecycl.apply {
            adapter = productsAdapter
        }

        lifecycleScope.launch {
            productViewModel.products.collect {
                when (it) {
                    is ApiState.Success<*> -> {

                        val data = it.data as? ListProductsResponse

                        val brands=data?.products


                        productsAdapter.submitList(brands)

                        Log.e("collectionId","${brands?.get(0)?.title}")

                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()

                    }
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                        // progressBar.visibility = View.GONE
                    }

                    else -> {

                        Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }


    }

    override fun onProductsClick(product: ProductsItem?) {
        TODO("Not yet implemented")
    }

    override fun onWishClick(product: ProductsItem?) {
        TODO("Not yet implemented")
    }

}