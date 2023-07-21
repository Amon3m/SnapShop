package com.itigradteamsix.snapshop.profile.view

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
import com.itigradteamsix.snapshop.data.models.Customer
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentProfileBinding
import com.itigradteamsix.snapshop.databinding.FragmentShoppingCartBinding
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.profile.viewmodel.ProfileViewModel
import com.itigradteamsix.snapshop.profile.viewmodel.ProfileViewModelFactory
import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModel
import com.itigradteamsix.snapshop.shoppingcart.viewmodel.ShoppingCartViewModelFactory
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
        binding.button.setOnClickListener {
            val action=ProfileFragmentDirections.actionProfileFragmentToSettingsFragment()
            Navigation.findNavController(view).navigate(action)
        }

        listenForCustomerFlow()


        // Initialize Firebase Authentication instance
        firebaseAuth = FirebaseAuth.getInstance()

        // Check if the user is logged in
        val currentUser = firebaseAuth.currentUser



        if (currentUser != null) {
            // User is logged in
            val uid = currentUser.uid
            val email = currentUser.email
            val displayName = currentUser.displayName
            val photoUrl = currentUser.photoUrl
            Log.d("ProfileFragment", "User is logged in with email: $email and uid: $uid and displayName: $displayName")

            profileViewModel.getCustomerByEmail(email!!)


        } else {
            // User is not logged in
        }
    }

    private fun listenForCustomerFlow() {
        lifecycleScope.launch {
            profileViewModel.customer.collect {
                when (it) {
                    is ApiState.Success<*> -> {
                        if (it.data is Customer) {
                            Log.d("ProfileFragment", "Success: ${it.data}")
                            binding.textView.text = it.data.first_name
                        }else{
                            Log.d("ProfileFragment", "Failure SUCCESSSSSS: ${it.data.toString()}")
                        }

                    }

                    is ApiState.Failure -> {
                        Log.d("ProfileFragment", "Error: ${it.msg}")
                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()

                    }

                    is ApiState.Loading -> {
                        Log.d("ProfileFragment", "Loading")
                    }

                    else -> {}
                }
            }
        }
    }

}

