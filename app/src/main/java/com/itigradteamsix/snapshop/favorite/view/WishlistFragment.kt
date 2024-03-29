package com.itigradteamsix.snapshop.favorite.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.StartActivity
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentWishlistBinding
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.favorite.model.FavoritePojo
import com.itigradteamsix.snapshop.favorite.model.LineItems
import com.itigradteamsix.snapshop.favorite.viewmodel.FavoriteViewModel
import com.itigradteamsix.snapshop.favorite.viewmodel.FavoriteViewModelFactory
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.settings.data.CurrencyPreferences
import com.itigradteamsix.snapshop.settings.data.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class WishlistFragment : Fragment() ,OnFavClickListener {
    private var favLineItems: ArrayList<LineItems> = arrayListOf()
    private var favoriteItems: ArrayList<FavoritePojo> = arrayListOf()
    private lateinit var binding: FragmentWishlistBinding
    private lateinit var viewModel: FavoriteViewModel
    private var draftOrderResponse : DraftOrderResponse = DraftOrderResponse()
    private lateinit var favAdapter:FavoriteAdapter
    var draftID : String? = null
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
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (userPreferences.isGuest){

            binding.progressBar7.visibility = GONE
            binding.groupGuest.visibility = View.VISIBLE
            binding.cardGuestLoggedIn.goToLoginButton.setOnClickListener {
                val intent = Intent(activity, StartActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }

        }else {


            val favoriteViewModelFactory = FavoriteViewModelFactory(
                Repository.getInstance(ApiClient, ConcreteLocalSource(requireContext())),
                FirebaseRepo(auth = FirebaseAuth.getInstance())
            )
            viewModel =
                ViewModelProvider(this, favoriteViewModelFactory)[FavoriteViewModel::class.java]
            favAdapter = FavoriteAdapter(ArrayList(), requireActivity(), this)
            binding.favRecycler.adapter = favAdapter
            val sharedPreferences =
                requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            draftID = sharedPreferences.getString("draftID", "")
//        if (activity != null) {
//            val intent = requireActivity().intent
//            if (intent != null) {
//                draftID = intent.getStringExtra("draftID")
//                Log.d("draftID", draftID!!)
//
//            }
//        }
            viewModel.getDraftOrder(draftID!!)
            viewLifecycleOwner.lifecycleScope.launch {

                viewModel.getDraftFlow.collect { result ->
                    when (result) {

                        is ApiDraftLoginState.Loading -> {
                            Log.d("favDraftFlowCollect", "Loading")
                        }

                        is ApiDraftLoginState.Success -> {
                            draftOrderResponse.draft_order = result.data
                            favoriteItems.clear()
                            favLineItems = result.data?.line_items as ArrayList<LineItems>
                            for (fav in favLineItems) {
                                favoriteItems.add(fav.toFavoritePojo())

                            }

                            favAdapter.setProductList(favoriteItems)
                            binding.progressBar7.visibility = GONE

                        }

                        is ApiDraftLoginState.Failure -> {
                            binding.progressBar7.visibility = GONE

                            Log.d("FavDraftFlowCollect", result.exception.message.toString())
                        }
                    }

                }
            }

        }
    }
    fun LineItems.toFavoritePojo(): FavoritePojo {
        return FavoritePojo(
            productId = product_id,
            price = price,
            imageSrc = applied_discount?.description,
            title = title,
            color = applied_discount?.title
        )
    }
    override fun onFavClickListener(product_Id: Long) {
        // get the product id by args.
        val action =
            WishlistFragmentDirections.actionWishlistFragmentToProductInfoFragment(product_Id)
        binding.root.findNavController().navigate(action)
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onDeleteClickListener(product_Id: Long) {
        val itemsToRemoveFromFavorite = mutableListOf<FavoritePojo>()
        val itemsToRemoveFromFavLineItems = mutableListOf<LineItems>()

        for (f in favoriteItems) {
            if (product_Id == f.productId) {
                itemsToRemoveFromFavorite.add(f)
                itemsToRemoveFromFavLineItems.addAll(
                    favLineItems.filter { favLineItem -> favLineItem.product_id == product_Id }
                )
                break
            }
        }

        favoriteItems.removeAll(itemsToRemoveFromFavorite)
        favLineItems.removeAll(itemsToRemoveFromFavLineItems)

        Log.d("favLineItems", favLineItems.toString())
        Log.d("favItems", favoriteItems.toString())

        draftOrderResponse.draft_order?.line_items = favLineItems
        viewModel.updateDraftOrder(draftID!!.toLong(), draftOrderResponse)

        favAdapter.notifyDataSetChanged()
    }

}