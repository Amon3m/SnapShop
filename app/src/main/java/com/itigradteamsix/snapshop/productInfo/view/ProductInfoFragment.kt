package com.itigradteamsix.snapshop.productInfo.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentProductInfoBinding
import com.itigradteamsix.snapshop.favorite.model.AppliedDiscount
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.favorite.model.LineItems
import com.itigradteamsix.snapshop.favorite.viewmodel.FavoriteViewModel
import com.itigradteamsix.snapshop.model.DraftOrder
import com.itigradteamsix.snapshop.model.Product
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.productInfo.viewmodel.ProductInfoViewModel
import com.itigradteamsix.snapshop.productInfo.viewmodel.ProductInfoViewModelFactory
import com.itigradteamsix.snapshop.settings.data.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProductInfoFragment : Fragment() {
    private lateinit var binding: FragmentProductInfoBinding
    private lateinit var viewModel: ProductInfoViewModel
    private lateinit var cartDraftOrderResponse: DraftOrder
    private var draftOrderResponse: DraftOrderResponse = DraftOrderResponse()
    private val args: ProductInfoFragmentArgs by navArgs()
    private var receivedProduct: Product? = null
    private var product_Id: Long? = null
    private var productQuantity: Int? = 1
    private var draftID : String? = null
    private var isFav : Boolean = false
    private lateinit var failureDialog: AlertDialog
    lateinit var userPreferences: UserPreferences





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            MyApplication.appInstance.settingsStore.userPreferencesFlow.first().let {
                userPreferences = it
            }
        }
    }

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
        viewModel = ViewModelProvider(
            this,
            productViewModelFactory
        )[ProductInfoViewModel::class.java]
        showLoading()
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

        viewModel.changeProductCartState(product_Id, 1, null)


        viewLifecycleOwner.lifecycleScope.launch {
            launch() {
                viewModel.productFlow.collect {
                    when (it) {
                        is ApiState.Loading -> {
                            Log.d("ProductInfoLoading", "Loading")

                        }

                        is ApiState.Success<*> -> {
                            showContent()
                            if (userPreferences.isGuest){
                                binding.favoriteBtn.visibility = View.GONE
                            }

                            receivedProduct = it.data as Product
                            setDataToViews(receivedProduct!!)

                        }

                        is ApiState.Failure -> {
                            binding.progressBar5.visibility = View.GONE

                            println(" Error ${it.msg}")
//                            showMsgDialog(it.msg)
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
//                                Log.d(
//                                    "outside if product=received",
//                                    "-" + item.product_id.toString() + "-" + receivedProduct?.id
//                                )
//                                Log.d("outside if product=received", receivedProduct.toString())
//                                Log.d("outside if product=received", item.toString())
//                                Log.d(
//                                    "outside if product=received",
//                                    draftOrderResponse.draft_order?.line_items!!.toString()
//                                )
//

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
                            Toast.makeText(MyApplication.appContext,"NO INTERNET!",Toast.LENGTH_SHORT).show()
//                            showMsgDialog(result.exception.message!!)
                        }
                    }

                }
            }
        }

        binding.decreasingQuantityBtn.setOnClickListener {
            if (productQuantity!! >=2){
                productQuantity = productQuantity!! - 1
//                binding.quantityTv.text = productQuantity.toString()
                viewModel.changeProductCartState(null, productQuantity, null)

            } else
                Toast.makeText(
                    requireContext(), "That's the minimum quantity",
                    Toast.LENGTH_SHORT
                ).show()
        }
        binding.addingQuantityBtn.setOnClickListener {
            if (productQuantity!! <receivedProduct!!.variants[0].inventory_quantity){
                productQuantity = productQuantity!! + 1
//                binding.quantityTv.text = productQuantity.toString()
                viewModel.changeProductCartState(null, productQuantity, null)
            } else
                Toast.makeText(
                    requireContext(), "Sorry no enough quantity in the inventory ",
                    Toast.LENGTH_SHORT
                ).show()
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
                    Log.d("mutableLine",receivedProduct!!.toLineItems().toString())
                    draftOrderResponse.draft_order?.line_items = mutableLineItems
                }
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_favorite_24)
                binding.favoriteBtn.setImageDrawable(drawable)
                viewModel.updateDraftOrder(draftID!!.toLong(), draftOrderResponse)
                isFav = true

            }
        }


        lifecycleScope.launch {
            viewModel.currentCartDraftOrder.collect { it ->
                when (it) {
                    is ApiState.Loading -> {
                        Log.d("currentCartDraftOrder", "Loading")
                    }

                    is ApiState.Success<*> -> {
//                        val cartDraftOrder = it.data as DraftOrder
//
//                        Log.d("currentCartDraftOrder", cartDraftOrder.toString())
//                        cartDraftOrderResponse = cartDraftOrder
//                        //check if the current product is already in the cart draft order line items
//                        cartDraftOrderResponse.line_items?.map { it.product_id }?.contains(receivedProduct?.id)?.let { it1 ->
//                            if (it1) {
//                                binding.addToCartBtn.text = "Remove from cart"
//                            }else{
//                                binding.addToCartBtn.text = "Add to cart"
//                            }
//                        }


                    }

                    is ApiState.Failure -> {
                        binding.addToCartBtn.text = "Add to cart"
                        binding.addToCartBtn.elevation = 0f
                        binding.addToCartBtn.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.grey
                            )
                        )
                        binding.addToCartBtn.setOnClickListener {
                            Toast.makeText(
                                requireContext(),
                                "Please login to add to cart",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


            }
        }

        lifecycleScope.launch {
            viewModel.currentProductIdAndQuantityIsAdded.collect {
                Log.d("currentProductIdAndQuantityIsAdded", it.toString())
                if (it.first == product_Id) {
                    if (it.third == true) {
                        binding.addToCartBtn.text = "Remove from cart"
                        binding.addToCartBtn.setOnClickListener {
                            viewModel.changeProductCartState(product_Id, 1, false)
                        }
                    } else {
                        binding.addToCartBtn.text = "Add to cart"
                        binding.addToCartBtn.setOnClickListener {
                            viewModel.changeProductCartState(product_Id, null, true)
                        }
                    }
                    binding.quantityTv.text = it.second.toString()


                }
            }
        }

    }

    private fun setDataToViews(product: Product) {

        binding.sizetv.text = product.options.get(0).values.toString()
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
                value = "10.0",
                title = variants[0].option2,
                amount = "20.00",
                value_type = "percentage"
            ),
            name = title,
            properties = arrayListOf(),
            custom = true,
            price = variants[0].price.toString(),
            variant_id = variants[0].id, variant_title = body_html.toString()
            , vendor = null
        )

    }

    override fun onPause() {
        super.onPause()
        viewModel.updateCart()
    }

    private fun showLoading() {
        binding.productimagedetails.visibility = View.GONE
        binding.cardView2.visibility = View.GONE
        binding.star.visibility = View.GONE
        binding.addingQuantityBtn.visibility = View.GONE
        binding.decreasingQuantityBtn.visibility = View.GONE
        binding.productName.visibility = View.GONE
        binding.price.visibility = View.GONE
        binding.detailsTv.visibility = View.GONE
        binding.addToCartBtn.visibility = View.GONE
        binding.favoriteBtn.visibility = View.GONE
        binding.rating.visibility = View.GONE
        binding.quantityTv.visibility = View.GONE
        binding.image3CardView.visibility = View.GONE
        binding.image2CardView.visibility = View.GONE
        binding.image1CardView.visibility = View.GONE
        binding.textView6.visibility = View.GONE
        binding.colortv.visibility = View.GONE
        binding.textView7.visibility = View.GONE
        binding.sizetv.visibility = View.GONE
        binding.reviewTitle.visibility = View.GONE
        binding.review1Container.visibility = View.GONE
        binding.review2Container.visibility = View.GONE
        binding.progressBar5.visibility = View.VISIBLE
    }
    private fun showContent() {
        binding.productimagedetails.visibility = View.VISIBLE
        binding.cardView2.visibility = View.VISIBLE
        binding.star.visibility = View.VISIBLE
        binding.addingQuantityBtn.visibility = View.VISIBLE
        binding.decreasingQuantityBtn.visibility = View.VISIBLE
        binding.productName.visibility = View.VISIBLE
        binding.price.visibility = View.VISIBLE
        binding.detailsTv.visibility = View.VISIBLE
        binding.addToCartBtn.visibility = View.VISIBLE
        binding.favoriteBtn.visibility = View.VISIBLE
        binding.rating.visibility = View.VISIBLE
        binding.quantityTv.visibility = View.VISIBLE
        binding.image3CardView.visibility = View.VISIBLE
        binding.image2CardView.visibility = View.VISIBLE
        binding.image1CardView.visibility = View.VISIBLE
        binding.textView6.visibility = View.VISIBLE
        binding.colortv.visibility = View.VISIBLE
        binding.textView7.visibility = View.VISIBLE
        binding.sizetv.visibility = View.VISIBLE
        binding.reviewTitle.visibility = View.VISIBLE
        binding.review1Container.visibility = View.VISIBLE
        binding.review2Container.visibility = View.VISIBLE
        binding.progressBar5.visibility = View.GONE
    }

    private fun showMsgDialog(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        val messageTextView = TextView(requireContext())

        messageTextView.text = message
        messageTextView.gravity = Gravity.CENTER

        val container = FrameLayout(requireContext())
        container.addView(messageTextView)

        builder.setView(container)
        builder.setCancelable(true)
        builder.setPositiveButton("Ok") { _: DialogInterface?, _: Int ->
        }
        failureDialog = builder.create()
        failureDialog.apply {
            setIcon(R.drawable.baseline_info_24)
            setTitle("Warning")
        }
            .show()
    }
}