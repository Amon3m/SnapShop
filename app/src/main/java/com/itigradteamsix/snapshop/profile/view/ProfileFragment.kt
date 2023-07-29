package com.itigradteamsix.snapshop.profile.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.StartActivity
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentProfileBinding
import com.itigradteamsix.snapshop.databinding.FragmentShoppingCartBinding
import com.itigradteamsix.snapshop.databinding.OrderItemBinding
import com.itigradteamsix.snapshop.model.Customer
import com.itigradteamsix.snapshop.model.OrdersItem
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.profile.viewmodel.ProfileViewModel
import com.itigradteamsix.snapshop.profile.viewmodel.ProfileViewModelFactory
import com.itigradteamsix.snapshop.settings.currency.CurrencyUtils
import com.itigradteamsix.snapshop.settings.data.CurrencyPreferences
import com.itigradteamsix.snapshop.settings.data.UserPreferences
import com.itigradteamsix.snapshop.settings.data.dataStore
import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModel
import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var profileViewModel: ProfileViewModel
    lateinit var userprefss: UserPreferences
    lateinit var currencyPreferences: CurrencyPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val profileViewModelFactory = ProfileViewModelFactory(
            Repository.getInstance(
                ApiClient,
                ConcreteLocalSource(requireContext())
            )
        )
        lifecycleScope.launch {
            userprefss = MyApplication.appInstance.settingsStore.userPreferencesFlow.first()
            currencyPreferences = MyApplication.appInstance.settingsStore.currencyPreferencesFlow.first()


        }
        profileViewModel = ViewModelProvider(this, profileViewModelFactory)[ProfileViewModel::class.java]
        if (!userprefss.isGuest) {
//            profileViewModel.getLastTwoOrders(userprefss.customerEmail)
            profileViewModel.getCustomerOrders(requireContext(), userprefss.customerEmail)
        }
        firebaseAuth = FirebaseAuth.getInstance()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.clSettings.setOnClickListener {
            val action=ProfileFragmentDirections.actionProfileFragmentToSettingsFragment()
            Navigation.findNavController(view).navigate(action)
        }



        binding.btnSignOut.setOnClickListener {
            firebaseAuth.signOut()
            profileViewModel.removeUserFromDataStore()
            val intent = Intent(activity, StartActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        binding.tvSeeAll.setOnClickListener {
            val action=ProfileFragmentDirections.actionProfileFragmentToOrdersFragment()
            Navigation.findNavController(view).navigate(action)
        }


        setScreenAccordingToPrefs()

        binding.btnGoToLogin.setOnClickListener {
            val intent = Intent(activity, StartActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }


        binding.clAddresses.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_profileFragment_to_addressFragment)
        }



    }

    private fun setScreenAccordingToPrefs(){

        lifecycleScope.launch {
            profileViewModel.userPreferencesFlow.collect{
                if(it.isGuest){
                    binding.groupGuest.visibility = View.VISIBLE
                    binding.groupNormal.visibility = View.GONE
                    binding.groupLoading.visibility = View.GONE
                    binding.groupNoInternet.visibility = View.GONE

                }else{
                    binding.groupGuest.visibility = View.GONE
                    binding.groupNormal.visibility = View.VISIBLE
                    binding.groupLoading.visibility = View.GONE
                    binding.groupNoInternet.visibility = View.GONE
                    listenForCustomerOrdersFlow()


                    binding.tvName.text = it.customerName.replace("null", "")

                    binding.tvEmail.text = it.customerEmail

                }
            }
        }
    }



    private fun listenForCustomerOrdersFlow() {
        lifecycleScope.launch {
            profileViewModel.lastTwoOrders.collect {
                when (it) {
                    is ApiState.Success<*> -> {
                        val orders = it.data as List<*>
                        if (orders.isNotEmpty()) {

                            binding.groupOrders.visibility = View.VISIBLE
                            val orderItemContainer1: OrderItemBinding = binding.include
                            val orderItemContainer2: OrderItemBinding = binding.include2

                            if (orders.isNotEmpty()) {
                                val order1 = orders[0] as OrdersItem
                                populateOrderItem(orderItemContainer1, order1)
                            }

                            if (orders.size >= 2) {
                                val order2 = orders[1] as OrdersItem
                                populateOrderItem(orderItemContainer2, order2)
                            }else{
                                binding.include2.root.visibility = View.GONE
                            }


                        } else {
                            binding.groupOrders.visibility = View.GONE
                        }

                    }

                    is ApiState.Failure -> {
                        binding.groupOrders.visibility = View.GONE

                    }

                    is ApiState.Loading -> {
                        binding.groupOrders.visibility = View.GONE

                    }

                    else -> {}
                }
            }
        }
    }

    private fun populateOrderItem(orderItemBinding: OrderItemBinding, order: OrdersItem) {
        orderItemBinding.tvOrderItemTitle.text = order.name
        orderItemBinding.tvOrderItemPrice.text = CurrencyUtils.convertCurrency(
            order.totalPrice?.toDoubleOrNull(),
            currencyPreferences)
        orderItemBinding.tvOrderCreatedAtDate .text =
            order.createdAt?.substring(0, 10)
        //set image of the order to first line item image in order (if exists)
        Log.d("TAG", "populateOrderItem LineItems: ${order.lineItems}")
        order.lineItems?.get(1)?.properties?.forEach { // (1) because first line item is Empty item
            Log.d("TAG", "populateOrderItem properties: ${it}")
            if (it.name == "image_url") {
                Log.d("TAG", "populateOrderItem: ${it.value}")
                Glide.with(requireContext())
                    .load(it.value)
                    .into(orderItemBinding.ivOrderItem)
            }
        }
    }

}

