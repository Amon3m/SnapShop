package com.itigradteamsix.snapshop.profile.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.StartActivity
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentProfileBinding
import com.itigradteamsix.snapshop.databinding.FragmentShoppingCartBinding
import com.itigradteamsix.snapshop.model.Customer
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.profile.viewmodel.ProfileViewModel
import com.itigradteamsix.snapshop.profile.viewmodel.ProfileViewModelFactory
import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModel
import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var profileViewModel: ProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val profileViewModelFactory = ProfileViewModelFactory(
            Repository.getInstance(
                ApiClient,
                ConcreteLocalSource(requireContext())
            )
        )
        profileViewModel = ViewModelProvider(this, profileViewModelFactory)[ProfileViewModel::class.java]
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
                        binding.groupOrders.visibility = View.VISIBLE

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

}

