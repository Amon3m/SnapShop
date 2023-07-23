package com.itigradteamsix.snapshop.products.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginTop
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


class CategoryFragment : Fragment(), OnProductsClickListener, FilterOptionsListener {

    lateinit var binding: FragmentCategoryBinding
    lateinit var productsAdapter: ProductsAdapter
    lateinit var productViewModel: ProductViewModel
    lateinit var productViewModelFactory: ProductViewModelFactory
    var collectionId: Long = 0
    var sortTitle: Boolean = false
    var sortPrice: Boolean = false
    var asc: Boolean = false
    var isCat: Boolean = false
    var fromPrice: Int = 0
    var toPrice: Int = 0
    var productType: String? = null
    var isFiltered: Boolean = false
    var isPrice: Boolean = false
    var isType: Boolean = false

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


        if (isCat) {
            binding.linearLayout.visibility = View.VISIBLE

            when (collectionId) {
                Utilities.MEN_COLLECTION_ID -> binding.menImageView.setColorFilter(R.color.black)
                Utilities.SALE_COLLECTION_ID -> binding.menImageView.setColorFilter(R.color.black)
                Utilities.WOMEN_COLLECTION_ID -> binding.menImageView.setColorFilter(R.color.black)
                Utilities.KIDS_COLLECTION_ID -> binding.menImageView.setColorFilter(R.color.black)
            }
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
                        Log.e(
                            "Filter option", "fromPrice:Int= $fromPrice" +
                                    "    var toPrice:Int=$toPrice" +
                                    "    var productType:String?= $productType" +
                                    "    var isFiltered:Boolean= $isFiltered" +
                                    "    var isPrice: Boolean=$isPrice" +
                                    "    var isType: Boolean=$isType"
                        )
                        val data = it.data as? ListProductsResponse

                        var brands = data?.products

                        if (sortTitle) {
                            if (asc) {

                                brands = brands?.sortedBy { it?.title }
                                if (isFiltered) {
                                    if (isPrice && !isType) {
                                        brands = makePriceFilter(fromPrice, toPrice, brands)
                                    } else if (isType && !isPrice) {
                                        brands = makeTypeFilter(productType, brands)
                                    } else {
                                        brands = makePriceFilter(fromPrice, toPrice, brands)
                                        brands = makeTypeFilter(productType, brands)

                                    }

                                }

                                productsAdapter.submitList(null)

                                productsAdapter.submitList(brands)

                            } else {
                                brands = brands?.sortedByDescending { it?.title }
                                if (isFiltered) {
                                    if (isPrice && !isType) {
                                        brands = makePriceFilter(fromPrice, toPrice, brands)
                                    } else if (isType && !isPrice) {
                                        brands = makeTypeFilter(productType, brands)
                                    } else {
                                        brands = makePriceFilter(fromPrice, toPrice, brands)
                                        brands = makeTypeFilter(productType, brands)

                                    }

                                }

                                productsAdapter.submitList(null)

                                productsAdapter.submitList(brands)

                            }
                        } else if (sortPrice) {
                            if (asc) {
                                brands =
                                    brands?.sortedBy { it?.variants?.get(0)?.price?.toDouble() }
                                if (isFiltered) {
                                    if (isPrice && !isType) {
                                        brands = makePriceFilter(fromPrice, toPrice, brands)
                                    } else if (isType && !isPrice) {
                                        brands = makeTypeFilter(productType, brands)
                                    } else {
                                        brands = makePriceFilter(fromPrice, toPrice, brands)
                                        brands = makeTypeFilter(productType, brands)

                                    }

                                }

                                productsAdapter.submitList(null)

                                productsAdapter.submitList(brands)

                            } else {
                                brands =
                                    brands?.sortedByDescending { it?.variants?.get(0)?.price?.toDouble() }

                                if (isFiltered) {
                                    if (isPrice && !isType) {
                                        brands = makePriceFilter(fromPrice, toPrice, brands)
                                    } else if (isType && !isPrice) {
                                        brands = makeTypeFilter(productType, brands)
                                    } else {
                                        brands = makePriceFilter(fromPrice, toPrice, brands)
                                        brands = makeTypeFilter(productType, brands)

                                    }

                                }

                                productsAdapter.submitList(null)
                                productsAdapter.submitList(brands)

                            }

                        } else {
                            if (isFiltered) {
                                if (isPrice && !isType) {
                                    brands = makePriceFilter(fromPrice, toPrice, brands)
                                } else if (isType && !isPrice) {
                                    brands = makeTypeFilter(productType, brands)
                                } else {
                                    brands = makePriceFilter(fromPrice, toPrice, brands)
                                    brands = makeTypeFilter(productType, brands)

                                }

                            }

                            productsAdapter.submitList(brands)

                        }


                    }

                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                        // progressBar.visibility = View.GONE
                    }

                    else -> {

                    }
                }

            }
        }

        binding.fab.setOnClickListener {

            val filterFragment = FilterBottomFragment()
            filterFragment.setFilterOptionsListener(this) // Set the listener
            filterFragment.show(childFragmentManager, filterFragment.tag)
        }
        binding.babyCard.setOnClickListener {
            collectionId = Utilities.KIDS_COLLECTION_ID
            productViewModel.getProducts(requireContext(), collectionId)
            isPrice = false
            isType = false
            isFiltered = false
            selectEffect()

        }
        binding.saleCard.setOnClickListener {
            collectionId = Utilities.SALE_COLLECTION_ID
            productViewModel.getProducts(requireContext(), collectionId)
            isPrice = false
            isType = false
            isFiltered = false
            selectEffect()

        }
        binding.menCard.setOnClickListener {
            collectionId = Utilities.MEN_COLLECTION_ID

            productViewModel.getProducts(requireContext(), collectionId)
            isPrice = false
            isType = false
            isFiltered = false
            selectEffect()

        }
        binding.womenCard.setOnClickListener {
            collectionId = Utilities.WOMEN_COLLECTION_ID
            productViewModel.getProducts(requireContext(), collectionId)
            isPrice = false
            isType = false
            isFiltered = false
            selectEffect()

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

    override fun onFilterOptionsSelected(
        minPrice: Int,
        maxPrice: Int,
        type: String?,
        isFilter: Boolean,
        isP: Boolean,
        isT: Boolean
    ) {
        fromPrice = minPrice
        toPrice = maxPrice
        productType = type
        isFiltered = isFilter
        isPrice = isP
        isType = isT

        productViewModel.getProducts(requireContext(), collectionId)

        Log.e("filter", "max: $fromPrice min: $toPrice productType: $type")
    }

    fun makePriceFilter(
        fromPrice: Int,
        toPrice: Int,
        product: List<ProductsItem?>?
    ): List<ProductsItem?>? {

        return product?.filter { product ->
            val price = product?.variants?.get(0)?.price?.toDouble() ?: 0.0

            price >= fromPrice && price <= toPrice
        }
    }

    fun makeTypeFilter(type: String?, product: List<ProductsItem?>?): List<ProductsItem?>? {

        return product?.filter { product ->
            val typeOfProduct = product?.productType

            typeOfProduct == type
        }
    }

    fun selectEffect() {
        when (collectionId) {
            Utilities.MEN_COLLECTION_ID -> {
                binding.menImageView.setColorFilter(R.color.black)
                binding.womenImageView.clearColorFilter()
                binding.babyImageView.clearColorFilter()
                binding.saleImageView.setBackgroundColor(getResources().getColor(R.color.md_theme_dark_inversePrimary))


            }

            Utilities.SALE_COLLECTION_ID -> {
                binding.saleImageView.setBackgroundColor(getResources().getColor(R.color.black))
                binding.womenImageView.clearColorFilter()
                binding.babyImageView.clearColorFilter()
                binding.menImageView.clearColorFilter()


            }

            Utilities.WOMEN_COLLECTION_ID -> {
                binding.womenImageView.setColorFilter(R.color.black)
                binding.babyImageView.clearColorFilter()
                binding.menImageView.clearColorFilter()
                binding.saleImageView.setBackgroundColor(getResources().getColor(R.color.md_theme_dark_inversePrimary))

            }

            Utilities.KIDS_COLLECTION_ID -> {
                binding.babyImageView.setColorFilter(R.color.black)
                binding.womenImageView.clearColorFilter()
                binding.menImageView.clearColorFilter()
                binding.saleImageView.setBackgroundColor(getResources().getColor(R.color.md_theme_dark_inversePrimary))


            }
        }
    }

}