package com.itigradteamsix.snapshop.categories.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.itigradteamsix.snapshop.Utilities
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentCategoryBinding
import com.itigradteamsix.snapshop.model.ListProductsResponse
import com.itigradteamsix.snapshop.model.ProductsItem
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.products.view.OnProductsClickListener
import com.itigradteamsix.snapshop.products.view.ProductsAdapter
import com.itigradteamsix.snapshop.products.viewmodel.ProductViewModel
import com.itigradteamsix.snapshop.products.viewmodel.ProductViewModelFactory
import kotlinx.coroutines.launch


class CategoryFragment : Fragment(), OnProductsClickListener {

    lateinit var binding: FragmentCategoryBinding
    lateinit var productsAdapter: ProductsAdapter
    lateinit var productViewModel: ProductViewModel
    lateinit var productViewModelFactory: ProductViewModelFactory
    var collectionId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModelFactory = ProductViewModelFactory(
            Repository.getInstance(
                ApiClient,
                ConcreteLocalSource(requireContext())
            )
        )

        productViewModel =
            ViewModelProvider(this, productViewModelFactory).get(ProductViewModel::class.java)
        
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectionId = CategoryFragmentArgs.fromBundle(requireArguments()).collectionId
        productViewModel.getProducts(requireContext(), collectionId)
        productsAdapter = ProductsAdapter(requireContext(), this)

        binding.catRecycl.apply {
            adapter = productsAdapter
        }

        lifecycleScope.launch {
            productViewModel.products.collect {
                when (it) {
                    is ApiState.Success<*> -> {

                        val data = it.data as? ListProductsResponse

                        val brands = data?.products


                        productsAdapter.submitList(brands)

                        Log.e("collectionId", "${brands?.get(0)?.title}")

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
        binding.babyCard.setOnClickListener {
            productViewModel.getProducts(requireContext(), Utilities.KIDS_COLLECTION_ID)
        }
        binding.saleCard.setOnClickListener {
            productViewModel.getProducts(requireContext(), Utilities.SALE_COLLECTION_ID)

        }
        binding.menCard.setOnClickListener {
            productViewModel.getProducts(requireContext(), Utilities.MEN_COLLECTION_ID)

        }
        binding.womenCard.setOnClickListener {
            productViewModel.getProducts(requireContext(), Utilities.WOMEN_COLLECTION_ID)
        }


    }

    override fun onProductsClick(product: ProductsItem?) {
        TODO("Not yet implemented")
    }

    override fun onWishClick(product: ProductsItem?) {
        TODO("Not yet implemented")
    }
}