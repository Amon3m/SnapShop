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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentShoppingCartBinding
import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.favorite.model.LineItems
import com.itigradteamsix.snapshop.model.LineItem
import com.itigradteamsix.snapshop.model.ProductListResponse
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.model.SmartCollectionResponse
import com.itigradteamsix.snapshop.model.calculateSavingAmount
import com.itigradteamsix.snapshop.model.getDiscountPercentage
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.settings.currency.CurrencyUtils
import com.itigradteamsix.snapshop.settings.currency.CurrencyUtils.convertCurrency
import com.itigradteamsix.snapshop.settings.data.CurrencyPreferences

import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModel
import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ShoppingCartFragment : Fragment() {

    lateinit var binding: FragmentShoppingCartBinding
    lateinit var shoppingCartViewModel: ShoppingCartViewModel
    lateinit var shoppingCartviewModelFactory: ShoppingCartViewModelFactory
    lateinit var cartAdapter: ShoppingCartAdapter
    lateinit var currencyPref: CurrencyPreferences

    //    var discountPercentage = 0
    var draftOrder = DraftOrder()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        shoppingCartviewModelFactory = ShoppingCartViewModelFactory(
            Repository.getInstance(
                ApiClient, ConcreteLocalSource(requireContext())
            )
        )
        shoppingCartViewModel = ViewModelProvider(
            requireActivity(), shoppingCartviewModelFactory
        )[ShoppingCartViewModel::class.java]

        lifecycleScope.launch {
            MyApplication.appInstance.settingsStore.currencyPreferencesFlow.first().let {
                currencyPref = it
            }
        }

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
        setUpCopounCode()

    }

    private fun setUpCopounCode() {
        binding.promoCodeApplyButton.setOnClickListener {
            val couponCode = binding.promoCodeEdittext.text.toString().trim()
            val discountPercentage = getDiscountPercentage(couponCode)

            shoppingCartViewModel.changeDiscountPercentage(discountPercentage)

        }

//        lifecycleScope.launch {
//            shoppingCartViewModel.discountPercentage.collectLatest {
//                if (it>0){
//                    Toast.makeText(requireContext(), "Coupon code applied ✔️️", Toast.LENGTH_SHORT).show()
//                    val totalItemsPrice = draftOrder.total_price?.toDouble()
//
//                    val discountPrice =
//                        calculateSavingAmount(totalItemsPrice!!, it)
//                    val totalPrice = totalItemsPrice.times(1 - it/100.0)
//
//                    binding.totalItemsPriceTextview.text = String.format("%.2f", totalItemsPrice)
//                    binding.discountPriceTextview.text = String.format("%.2f", discountPrice)
//                    binding.discountPriceTextview.setTextColor(resources.getColor(R.color.md_theme_light_surfaceTint))
//                    binding.totalPriceTextview.text = String.format("%.2f", totalPrice)
//
//                }else{
////                    Toast.makeText(requireContext(), "Invalid coupon code", Toast.LENGTH_SHORT).show()
//
//                    binding.totalItemsPriceTextview.text = draftOrder.total_price.toString()
//                    binding.totalPriceTextview.text = draftOrder.total_price.toString()
//                    binding.discountPriceTextview.setTextColor(resources.getColor(R.color.black))
//                    binding.discountPriceTextview.text = "0.0"
//                }
//            }
//
//        }


    }

    private fun setUpCartRecyclerView() {
        cartAdapter = ShoppingCartAdapter(
            currencyPref,
            onIncreaseClickListener = { lineItem ->
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
            }
        )
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
                        this@ShoppingCartFragment.draftOrder = cartDraftOrder
                        //filter out the line items with title contains "dummy" or "empty"
                        val filteredLineItems = cartDraftOrder.line_items?.filter { lineItem ->
                            !lineItem.title?.contains("dummy", true)!! && !lineItem.title.contains(
                                "empty", true
                            )
                        }
                        cartAdapter.submitList(filteredLineItems)

//                        binding.totalItemsPriceTextview.text = cartDraftOrder.total_price.toString()
//                        binding.totalPriceTextview.text = cartDraftOrder.total_price.toString()

                        binding.checkoutButton.setOnClickListener { view ->
                            val action =
                                ShoppingCartFragmentDirections.actionShoppingCartFragmentToOrderReview()
                            Navigation.findNavController(view).navigate(action)

                        }
                    }

                    is ApiState.Failure -> {
                        binding.shoppingCartGroup.visibility = View.GONE
                        binding.loadingCartGroup.visibility = View.GONE
                        binding.emptyCartGroup.visibility = View.VISIBLE
//                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }

                shoppingCartViewModel.discountPercentage.collectLatest {
                    if (it > 0) {
                        Toast.makeText(
                            requireContext(),
                            "Coupon code applied ✔️️",
                            Toast.LENGTH_SHORT
                        ).show()
                        val totalItemsPrice = draftOrder.total_price?.toDouble()

                        val discountPrice =
                            calculateSavingAmount(totalItemsPrice!!, it)
                        val totalPrice = totalItemsPrice.times(1 - it / 100.0)

                        binding.totalItemsPriceTextview.text =
                            "$totalItemsPrice ${currencyPref.currencySymbol}"
//                            convertCurrency(
//                            totalItemsPrice,
//                            currencyPref
//                        )
                        binding.discountPriceTextview.text =
                            "- $discountPrice ${currencyPref.currencySymbol}"
//                            convertCurrency(
//                            discountPrice,
//                            currencyPref
//                        )
                        binding.discountPriceTextview.setTextColor(resources.getColor(R.color.md_theme_light_surfaceTint))
                        binding.totalPriceTextview.text =
                            "$totalPrice ${currencyPref.currencySymbol}"
//                        convertCurrency(
//                            totalPrice,
//                            currencyPref
//                        )

                    } else {
//                    Toast.makeText(requireContext(), "Invalid coupon code", Toast.LENGTH_SHORT).show()

                        binding.totalItemsPriceTextview.text =
                            "${currencyPref.currencySymbol} ${draftOrder.total_price}"
//                            convertCurrency(
//                            draftOrder.total_price?.toDouble(),
//                            currencyPref
//                        )
                        binding.totalPriceTextview.text =
                            "${currencyPref.currencySymbol} ${draftOrder.total_price}"
//                            convertCurrency(
//                            draftOrder.total_price?.toDouble(),
//                            currencyPref
//                        )
                        binding.discountPriceTextview.setTextColor(resources.getColor(R.color.black))
                        binding.discountPriceTextview.text = "${currencyPref.currencySymbol} 0.0"
//                            "${currencyPref.currencySymbol} 0.0"
                    }
                }


            }
        }
    }


}

