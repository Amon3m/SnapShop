package com.itigradteamsix.snapshop.shoppingcart.order

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpPost
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentOrderReviewBinding
import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.model.calculateSavingAmount
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.settings.data.CurrencyPreferences
import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModel
import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModelFactory
import com.stripe.android.Stripe
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.PaymentSheetResultCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class OrderReview : Fragment() {
    lateinit var binding: FragmentOrderReviewBinding
    lateinit var shoppingCartViewModel: ShoppingCartViewModel
    lateinit var orderReviewAdapter: OrderReviewAdapter

    private lateinit var paymentSheet: PaymentSheet
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    lateinit var paymentIntentClientSecret: String
    private lateinit var paymentService: PaymentService
    var totalPrice: Double = 0.0
    lateinit var currencyPref : CurrencyPreferences


    var draftOrder = DraftOrder()


    val TAG = "OrderReview"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val shoppingCartviewModelFactory = ShoppingCartViewModelFactory(
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


        // Initialize the Stripe SDK with your publishable key
        Stripe(
            MyApplication.appContext,
            "pk_test_51NVjXWGgec2y6RBTEnPK2fhQBQat9EHhrF78p2qKuNRRmhwQXZy6IoU3Ir9jWZBfLz1fwwcJfRVKKjvTXcEMVvn400yhB7jfi5"
        ).also {
            paymentSheet = PaymentSheet(
                this,
                ::onPaymentResult
            )
        }

        // Set up Retrofit and create the API service
        val retrofit = Retrofit.Builder()
            .baseUrl("http://49.12.102.5:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        paymentService = retrofit.create(PaymentService::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderReviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCartRecyclerView()
        handlePlacingOrder()
        handleCopouns()

    }


    @SuppressLint("SetTextI18n")
    private fun handleCopouns(){
        lifecycleScope.launch {
            shoppingCartViewModel.discountPercentage.collectLatest {
                if (it>0){
                    Toast.makeText(requireContext(), "Coupon code applied ✔️️", Toast.LENGTH_SHORT).show()
                    val totalItemsPrice = draftOrder.total_price?.toDouble()

                    val discountPrice =
                        calculateSavingAmount(totalItemsPrice!!, it)
                    val totalPrice = totalItemsPrice.times(1 - it/100.0)
                    this@OrderReview.totalPrice = totalPrice

                    binding.totalItemsPriceTextview.text = "$totalItemsPrice ${currencyPref.currencySymbol}"
                    binding.discountPriceTextview.text = "- $discountPrice ${currencyPref.currencySymbol}"
                    binding.discountPriceTextview.setTextColor(resources.getColor(R.color.md_theme_light_surfaceTint))
                    binding.totalPriceTextview.text = "$totalPrice ${currencyPref.currencySymbol}"

                }else{
//                    Toast.makeText(requireContext(), "Invalid coupon code", Toast.LENGTH_SHORT).show()

                    binding.totalItemsPriceTextview.text ="${currencyPref.currencySymbol} ${draftOrder.total_price}"
                    binding.totalPriceTextview.text = "${currencyPref.currencySymbol} ${draftOrder.total_price}"
                    this@OrderReview.totalPrice = draftOrder.total_price?.toDoubleOrNull() ?: 0.0
                    binding.discountPriceTextview.setTextColor(resources.getColor(R.color.black))
                    binding.discountPriceTextview.text = "- ${currencyPref.currencySymbol} 0.0"
                }
            }

        }
    }







    private fun setUpCartRecyclerView() {
        orderReviewAdapter = OrderReviewAdapter()
        binding.orderReviewRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.orderReviewRV.adapter = orderReviewAdapter
        lifecycleScope.launch {
            shoppingCartViewModel.cartDraftOrder.collectLatest {
                when (it) {
                    is ApiState.Success<*> -> {
                        val cartDraftOrder = it.data as DraftOrder
                        draftOrder = cartDraftOrder

                        val filteredLineItems = cartDraftOrder.line_items?.filter { lineItem ->
                            !lineItem.title?.contains("dummy", true)!! && !lineItem.title.contains(
                                "empty", true
                            )
                        }
                        orderReviewAdapter.submitList(filteredLineItems)

//                        binding.totalItemsPriceTextview.text = cartDraftOrder.total_price.toString()
//                        binding.totalPriceTextview.text = cartDraftOrder.total_price.toString()
//                        totalPrice = cartDraftOrder.total_price?.toDoubleOrNull() ?: 0.0

                    }

                    else -> {}
                }
            }
        }
    }

    private fun handlePlacingOrder() {



        binding.placeOrder.setOnClickListener {
            Log.d(TAG, "hnandlePlacingOrder: $totalPrice")

            if (binding.visaCard.isChecked) {
                //make payment with stripe
                val paymentAmount = totalPrice * 100
                createPaymentIntent(paymentAmount)

            } else if (binding.cashOnDelivery.isChecked) {
                //make an order
                shoppingCartViewModel.completeDraftOrder()
            }else{
                Toast.makeText(requireContext(), "Please select a payment method", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            shoppingCartViewModel.orderCompleteState.collect {
                if (it) {
                    Toast.makeText(MyApplication.appContext, "Order Placed, Thank you!", Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }
            }
        }

    }

    private fun onPaymentResult(paymentResult: PaymentSheetResult) {
        when (paymentResult) {
            is PaymentSheetResult.Canceled -> {
                showToast("Payment Canceled")
            }

            is PaymentSheetResult.Failed -> {
                showToast("Payment Failed")

            }

            is PaymentSheetResult.Completed -> {
                showToast("Payment Completed")
                shoppingCartViewModel.completeDraftOrder() //makes an order and make a new draft order

            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }




    private fun createPaymentIntent(amount: Double) {
        // Call your backend API to create a payment intent
        paymentService.createPaymentIntent(amount, currencyPref.currencyCode.lowercase()) //1000, "usd" for example
            .enqueue(object : Callback<PaymentIntentResponse> {
                override fun onResponse(
                    call: Call<PaymentIntentResponse>,
                    response: Response<PaymentIntentResponse>
                ) {
                    if (response.isSuccessful) {
                        val clientSecret = response.body()?.clientSecret
                        if (clientSecret != null) {
                            paymentSheet.presentWithPaymentIntent(
                                clientSecret,
                                PaymentSheet.Configuration(
                                    merchantDisplayName = "Snap Shop",
                                )
                            )
                        } else {
                            showToast("Payment Intent creation failed.")
                        }
                    } else {
                        showToast("Payment Intent creation failed.")
                    }
                }

                override fun onFailure(call: Call<PaymentIntentResponse>, t: Throwable) {
                    showToast("Payment Intent creation failed: ${t.message}")
                }
            })
    }


}


