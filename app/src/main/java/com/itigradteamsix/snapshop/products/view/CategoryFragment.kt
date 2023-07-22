package com.itigradteamsix.snapshop.products.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.Utilities
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentCategoryBinding
import com.itigradteamsix.snapshop.model.ListProductsResponse
import com.itigradteamsix.snapshop.model.ProductsItem
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.products.viewmodel.ProductViewModel
import com.itigradteamsix.snapshop.products.viewmodel.ProductViewModelFactory
import kotlinx.coroutines.launch


class CategoryFragment : Fragment(), OnProductsClickListener {

    lateinit var binding: FragmentCategoryBinding
    lateinit var productsAdapter: ProductsAdapter
    lateinit var productViewModel: ProductViewModel
    lateinit var productViewModelFactory: ProductViewModelFactory
    var collectionId: Long = 0
    var sortTitle: Boolean = false
    var sortPrice: Boolean = false
    var asc: Boolean = false
    var isCat:Boolean=false
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
        isCat = CategoryFragmentArgs.fromBundle(requireArguments()).comeFromCat

        if (isCat){
            binding.linearLayout.visibility=View.VISIBLE
        }


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

                        var brands = data?.products

                        if (sortTitle) {
                            if (asc) {
                                brands = brands?.sortedBy { it?.title }
                                productsAdapter.submitList(null)

                                productsAdapter.submitList(brands)

                            } else {
                                brands = brands?.sortedByDescending { it?.title }
                                productsAdapter.submitList(null)

                                productsAdapter.submitList(brands)

                            }
                        } else if (sortPrice) {
                            if (asc) {
                                brands = brands?.sortedBy { it?.variants?.get(0)?.price?.toDouble() }
                                productsAdapter.submitList(null)

                                productsAdapter.submitList(brands)

                            } else {
                                brands = brands?.sortedByDescending { it?.variants?.get(0)?.price?.toDouble() }
                                productsAdapter.submitList(null)
                                productsAdapter.submitList(brands)

                            }

                        } else {
                            productsAdapter.submitList(brands)

                        }

                        Log.e("collectionId", "${brands?.get(0)?.title}")

                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
//                        binding.catRecycl.scrollToPosition(1)


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
            collectionId = Utilities.KIDS_COLLECTION_ID
            productViewModel.getProducts(requireContext(), collectionId)
        }
        binding.saleCard.setOnClickListener {
            collectionId = Utilities.SALE_COLLECTION_ID
            productViewModel.getProducts(requireContext(), collectionId)

        }
        binding.menCard.setOnClickListener {
            collectionId = Utilities.MEN_COLLECTION_ID

            productViewModel.getProducts(requireContext(), collectionId)

        }
        binding.womenCard.setOnClickListener {
            collectionId = Utilities.WOMEN_COLLECTION_ID
            productViewModel.getProducts(requireContext(), collectionId)
        }

        binding.sortTitle.setOnClickListener {
            sortTitle = true
            sortPrice = false
            asc = !asc
            binding.sortTitleSign.visibility = View.VISIBLE

            binding.sortPriceSign.visibility = View.GONE

            productViewModel.getProducts(requireContext(), collectionId)

            if (asc) {
                binding.sortTitleSign.text = getString(R.string.down_arrow)
            } else
                binding.sortTitleSign.text = getString(R.string.up_arrow)
        }
        binding.sortPrice.setOnClickListener {
            sortTitle = false
            sortPrice = true
            asc = !asc
            binding.sortTitleSign.visibility = View.GONE
            binding.sortPriceSign.visibility = View.VISIBLE
            productViewModel.getProducts(requireContext(), collectionId)

            if (asc) {
                binding.sortPriceSign.text = getString(R.string.down_arrow)
            } else
                binding.sortPriceSign.text = getString(R.string.up_arrow)
        }


    }

    override fun onProductsClick(product: ProductsItem?) {
        TODO("Not yet implemented")
    }

    override fun onWishClick(product: ProductsItem?) {
        TODO("Not yet implemented")
    }
}