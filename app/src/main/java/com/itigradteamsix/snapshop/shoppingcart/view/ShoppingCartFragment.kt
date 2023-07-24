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
import androidx.recyclerview.widget.LinearLayoutManager
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentShoppingCartBinding
import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.favorite.model.LineItems
import com.itigradteamsix.snapshop.model.LineItem
import com.itigradteamsix.snapshop.model.ProductListResponse
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.model.SmartCollectionResponse
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState

import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModel
import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ShoppingCartFragment : Fragment() {

    lateinit var binding: FragmentShoppingCartBinding
    lateinit var shoppingCartViewModel: ShoppingCartViewModel
    lateinit var shoppingCartviewModelFactory: ShoppingCartViewModelFactory
    lateinit var cartAdapter: ShoppingCartAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shoppingCartviewModelFactory = ShoppingCartViewModelFactory(
            Repository.getInstance(
                ApiClient, ConcreteLocalSource(requireContext())
            )
        )
        shoppingCartViewModel = ViewModelProvider(
            this, shoppingCartviewModelFactory
        )[ShoppingCartViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        shoppingCartViewModel.getCartDraftOrder()
        setUpCartRecyclerView()

    }

    private fun setUpCartRecyclerView() {
        cartAdapter = ShoppingCartAdapter(onIncreaseClickListener = { lineItem ->
            shoppingCartViewModel.increaseQuantity(lineItem)
        }, onDecreaseClickListener = { lineItem ->
            shoppingCartViewModel.decreaseQuantity(lineItem)
        }, onDeleteClickListener = { lineItem ->
            shoppingCartViewModel.deleteLineItem(lineItem)
        }, onItemClicked = { lineItem ->
//                //navigate to the product details fragment
//                val action = ShoppingCartFragmentDirections.actionShoppingCartFragmentToProductDetailsFragment(
//                    lineItem.product_id.toString()
//                )
//                findNavController().navigate(action)
        })
        binding.shoppingCartRecyclerview.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        lifecycleScope.launch {
            shoppingCartViewModel.cartDraftOrder.collectLatest {
                when (it) {
                    is ApiState.Loading -> {
                        binding.shoppingCartGroup.visibility = View.GONE
                        binding.emptyCartGroup.visibility = View.GONE
                        binding.loadingCartGroup.visibility = View.VISIBLE
                    }

                    is ApiState.Success<*> -> {
                        binding.shoppingCartGroup.visibility = View.VISIBLE
                        binding.emptyCartGroup.visibility = View.GONE
                        binding.loadingCartGroup.visibility = View.GONE
                        val cartDraftOrder = it.data as DraftOrder
                        cartAdapter.submitList(cartDraftOrder.line_items?.filter { lineItem ->
                            lineItem.title != "empty"
                        })
                        binding.totalItemsPriceTextview.text = cartDraftOrder.total_price.toString()
                        binding.totalPriceTextview.text = cartDraftOrder.total_price.toString()
                    }

                    is ApiState.Failure -> {
                        binding.shoppingCartGroup.visibility = View.GONE
                        binding.loadingCartGroup.visibility = View.GONE
                        binding.emptyCartGroup.visibility = View.VISIBLE
//                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}

