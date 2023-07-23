package com.itigradteamsix.snapshop.productInfo.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentProductInfoBinding
import com.itigradteamsix.snapshop.favorite.model.AppliedDiscount
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.favorite.model.LineItems
import com.itigradteamsix.snapshop.favorite.viewmodel.FavoriteViewModel
import com.itigradteamsix.snapshop.model.Product
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.productInfo.viewmodel.ProductInfoViewModel
import com.itigradteamsix.snapshop.productInfo.viewmodel.ProductInfoViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductInfoFragment : Fragment() {
    private lateinit var binding: FragmentProductInfoBinding
    private lateinit var viewModel: ProductInfoViewModel
    private var draftOrderResponse : DraftOrderResponse = DraftOrderResponse()
    private val args: ProductInfoFragmentArgs by navArgs()
    private var receivedProduct: Product? = null
    private var product_Id: Long? = null
    private var productQuantity: Int? = 1
    private var draftID : String? = null
    private var isFav : Boolean = false





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("onviewcrreated","new")
        super.onViewCreated(view, savedInstanceState)
        val  productViewModelFactory= ProductInfoViewModelFactory(
            Repository.getInstance(ApiClient, ConcreteLocalSource(requireContext())),
            FirebaseRepo(auth = FirebaseAuth.getInstance())
        )
        viewModel = ViewModelProvider(requireActivity(),productViewModelFactory)[ProductInfoViewModel::class.java]
//        if (activity != null) {
//            val intent = requireActivity().intent
//            if (intent != null) {
//                draftID = intent.getStringExtra("draftID")
////                Log.d("draftID", draftID!!)
//
//            }
//        }
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        draftID = sharedPreferences.getString("draftID","")
        draftID?.let { viewModel.getDraftOrder(it) }
        product_Id = args.productId
        Log.d("ProductInfoArgsId",product_Id.toString())
        viewModel.getSingleProduct(product_Id!!,requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            launch() {
                viewModel.productFlow.collect {
                    when (it) {
                        is ApiState.Loading -> {
                            Log.d("ProductInfoLoading", "Loading")

                        }

                        is ApiState.Success<*> -> {
                            receivedProduct = it.data as Product
                            setDataToViews(receivedProduct!!)

                        }

                        is ApiState.Failure -> {
                            println(" Error ${it.msg}")
                        }
                    }
                }
            }


            launch {
                delay(1000)
                viewModel.getDraftFlow.collect { result ->
                    when (result) {

                        is ApiDraftLoginState.Loading -> {
                            Log.d("PIDraftFlowCollect", "Loading")
                        }

                        is ApiDraftLoginState.Success -> {
                            draftOrderResponse.draft_order = result.data
                            for (item in draftOrderResponse.draft_order?.line_items!!) {
                                Log.d(
                                    "outside if product=received",
                                    "-" + item.product_id.toString() + "-" + receivedProduct?.id
                                )
                                Log.d("outside if product=received", receivedProduct.toString())
                                Log.d("outside if product=received", item.toString())
                                Log.d(
                                    "outside if product=received",
                                    draftOrderResponse.draft_order?.line_items!!.toString()
                                )


                                if (item.product_id == receivedProduct?.id) {
                                    Log.d(
                                        "inside if product=received",
                                        product_Id.toString() + " " + receivedProduct?.id
                                    )

                                    val drawable = ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.baseline_favorite_24
                                    )
                                    binding.favoriteBtn.setImageDrawable(drawable)
                                    isFav = true
                                    break
                                }
                            }
                        }

                        is ApiDraftLoginState.Failure -> {
                            Log.d("PIDraftFlowCollect", result.exception.message.toString())
                        }
                    }

                }
            }
        }

        binding.decreasingQuantityBtn.setOnClickListener {
            if (productQuantity!! >=2){
                productQuantity = productQuantity!! - 1
                binding.quantityTv.text = productQuantity.toString()
            }
            else
                Toast.makeText(requireContext(),"That's the minimum quantity",
                Toast.LENGTH_SHORT).show()
        }
        binding.addingQuantityBtn.setOnClickListener {
            if (productQuantity!! <receivedProduct!!.variants[0].inventory_quantity){
                productQuantity = productQuantity!! + 1
                binding.quantityTv.text = productQuantity.toString()
            }
            else
                Toast.makeText(requireContext(),"Sorry no enough quantity in the inventory ",
                    Toast.LENGTH_SHORT).show()
        }
        binding.image1.setOnClickListener {
            val drawable = binding.image1.drawable
            binding.productimagedetails.setImageDrawable(drawable)        }

        binding.image2.setOnClickListener {
            val drawable = binding.image2.drawable
            binding.productimagedetails.setImageDrawable(drawable)        }

        binding.image3.setOnClickListener {
            val drawable = binding.image3.drawable
            binding.productimagedetails.setImageDrawable(drawable)        }

        binding.favoriteBtn.setOnClickListener {

            if (isFav) {
                val lineItems = draftOrderResponse.draft_order?.line_items

                if (lineItems != null) {
                    val mutableLineItems = lineItems.toMutableList()

                    val iterator = mutableLineItems.iterator()
                    while (iterator.hasNext()) {
                        val item = iterator.next()
                        if (item.product_id == receivedProduct?.id) {
                            iterator.remove()
                        }
                    }
                    Log.d("mutableLine",mutableLineItems.toString())
                    draftOrderResponse.draft_order?.line_items = mutableLineItems
                }

                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_favorite_border_24)
                binding.favoriteBtn.setImageDrawable(drawable)
                viewModel.updateDraftOrder(draftID!!.toLong(), draftOrderResponse)
                isFav = false
            }

            else{

                val lineItems = draftOrderResponse.draft_order?.line_items

                if (lineItems != null) {
                    val mutableLineItems = lineItems.toMutableList()
                    mutableLineItems.add(receivedProduct!!.toLineItems())
                    Log.d("mutableLine",mutableLineItems.toString())
                    draftOrderResponse.draft_order?.line_items = mutableLineItems
                }

                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_favorite_24)
                binding.favoriteBtn.setImageDrawable(drawable)
                viewModel.updateDraftOrder(draftID!!.toLong(), draftOrderResponse)
                isFav = true

            }
        }
}

    private fun setDataToViews(product: Product) {


        binding.colortv.text = product.variants[0].option2
        binding.detailsTv.text = product.body_html
        binding.price.text = product.variants[0].price
        binding.productName.text = product.title
        binding.rating.text = getLastTwoDigitsAsDouble(product.id).toString()
        Glide.with(requireContext())
            .load(product.image.src)
            .into(binding.productimagedetails)
       if (product.images.size>2) {
           Glide.with(requireContext())
               .load(product.images[2].src)
               .into(binding.image3)
       }
        else{
           Glide.with(requireContext())
               .load(product.image.src)
               .into(binding.image3)
        }
        if (product.images.size>1) {
            Glide.with(requireContext())
                .load(product.images[1].src)
                .into(binding.image2)
        }
        if (product.images.isNotEmpty()) {
            Glide.with(requireContext())
                .load(product.images[0].src)
                .into(binding.image1)
        }

    }
    fun getLastTwoDigitsAsDouble(number: Long): Double {
        val lastTwoDigits = number % 100
        return lastTwoDigits.toDouble() / 20.0
    }

    fun Product.toLineItems(): LineItems {
        Log.d("imageee",image.src.toString())

        return LineItems(

            product_id = id,
            title = title,
            sku = image.src,
            quantity = 1,
            requires_shipping = false,
            taxable = true,
            gift_card = false,
            fulfillment_service = "manual",
            grams = 0,
            tax_lines = arrayListOf(),
            applied_discount = AppliedDiscount(
                description = image.src,

                value = "",
                title = "",
                amount = "",
                value_type = ""
            ),
            name = title,
            properties = arrayListOf(),
            custom = true,
            price = variants[0].price.toString(),
            variant_id = variants[0].id, variant_title = body_html.toString()
            , vendor = null
        )

    }

}