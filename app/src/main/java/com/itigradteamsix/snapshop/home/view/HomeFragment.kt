package com.itigradteamsix.snapshop.home.view

import android.content.ContentValues
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.Utilities
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentHomeBinding
import com.itigradteamsix.snapshop.home.viewmodel.HomeViewModel
import com.itigradteamsix.snapshop.home.viewmodel.HomeViewModelFactory
import com.itigradteamsix.snapshop.model.Product
import com.itigradteamsix.snapshop.model.ProductListResponse
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.model.SmartCollectionsItem
import com.itigradteamsix.snapshop.model.SmartCollectionsResponse
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var adapter: SearchAdapter
    var productsList = mutableListOf<Product>()
    private val searchQueryFlow = MutableStateFlow("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModelFactory = HomeViewModelFactory(
            Repository.getInstance(
                ApiClient,
                ConcreteLocalSource(requireContext())
            )
        )

        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        adapter = SearchAdapter(requireContext())
        binding.searchRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.searchRv.adapter = adapter

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getSmartCollections(requireContext())
        homeViewModel.getAllProducts(requireContext())
        lifecycleScope.launch {
            homeViewModel.smartCollection.collect {
                when (it) {
                    is ApiState.Success<*> -> {

                        val data = it.data as? SmartCollectionsResponse

                        val brands = data?.smartCollections

                        bindingData(brands)
                        Log.e("src", "${brands?.get(0)?.title}")

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
        lifecycleScope.launch {
            homeViewModel.productList.collect{

                when (it) {
                    is ApiState.Success<*> -> {

                        val data = it.data as? ProductListResponse
                        productsList = data?.products as MutableList<Product>
                        for(item in productsList)
                        Log.d("list",item.title.toString())
                        updateFilteredProductsList()

                    }

                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    }

                    else -> {

                    }
                }

            }
        }
        lifecycleScope.launch {
            searchQueryFlow
                .debounce(300)
                .distinctUntilChanged()
                .collect { updateFilteredProductsList() }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQueryFlow.value = s.toString()
                updateRecyclerViewVisibility(s.toString())

            }

            override fun afterTextChanged(editable: Editable?) {
            }
        }

        binding.searchEditText.addTextChangedListener(textWatcher)
        binding.moreTxt.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToBrandsFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }

        binding.saleCard.setOnClickListener { goToCategory(Utilities.SALE_COLLECTION_ID)}
        binding.babyCard.setOnClickListener { goToCategory(Utilities.KIDS_COLLECTION_ID)}
        binding.menCard.setOnClickListener {goToCategory(Utilities.MEN_COLLECTION_ID) }
        binding.womenCard.setOnClickListener {goToCategory(Utilities.WOMEN_COLLECTION_ID) }





    }

    fun goToCategory(id: Long){
        val action=HomeFragmentDirections.actionHomeFragmentToCategoryFragment(collectionId = id,true)
        Navigation.findNavController(requireView()).navigate(action)

    }

    fun goToProduct(id: Long?) {
        if (id != null) {
            val action =
                HomeFragmentDirections.actionHomeFragmentToCategoryFragment(collectionId = id,false)
            Navigation.findNavController(requireView()).navigate(action)
        }

    }

    private fun bindingData(brands: List<SmartCollectionsItem?>?) {

        binding.brand1Card.setOnClickListener { goToProduct(brands?.get(0)?.id) }
        binding.brand2Card.setOnClickListener { goToProduct(brands?.get(6)?.id) }
        binding.brand3Card.setOnClickListener { goToProduct(brands?.get(2)?.id) }
        binding.brand4Card.setOnClickListener { goToProduct(brands?.get(8)?.id) }

        Glide.with(requireContext())
            .load(brands?.get(0)?.image?.src)
            .placeholder(R.drawable.img_5)
            .error(R.drawable.img_6)
            .into(binding.brand1ImageView)

        Glide.with(requireContext())
            .load(brands?.get(6)?.image?.src)
            .placeholder(R.drawable.img_5)
            .error(R.drawable.img_6)
            .into(binding.brand2ImageView)

        Glide.with(requireContext())
            .load(brands?.get(2)?.image?.src)
            .placeholder(R.drawable.img_5)
            .error(R.drawable.img_6)
            .into(binding.brand3ImageView)

        Glide.with(requireContext())
            .load(brands?.get(8)?.image?.src)
            .placeholder(R.drawable.img_5)
            .error(R.drawable.img_6)
            .into(binding.brand4ImageView)


    }
    private fun updateFilteredProductsList() {
        val filteredProducts = productsList.filter { product ->
            product.title.startsWith(searchQueryFlow.value, ignoreCase = true)
        }
        adapter.submitList(filteredProducts)
    }
    private fun updateRecyclerViewVisibility(query: String) {
        if (query.isNotEmpty()) {
            binding.searchRv.visibility = View.VISIBLE
            binding.searchRv.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        } else {
            binding.searchRv.visibility = View.GONE
            binding.searchRv.layoutParams.height = 0

        }
    }

}