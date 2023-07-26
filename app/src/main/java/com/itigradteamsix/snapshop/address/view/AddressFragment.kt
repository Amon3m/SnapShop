package com.itigradteamsix.snapshop.address.view

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.address.viewmodel.AddressViewModel
import com.itigradteamsix.snapshop.address.viewmodel.AddressViewModelFactory
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentAddressBinding
import com.itigradteamsix.snapshop.favorite.model.LineItems
import com.itigradteamsix.snapshop.favorite.view.FavoriteAdapter
import com.itigradteamsix.snapshop.favorite.view.WishlistFragmentDirections
import com.itigradteamsix.snapshop.favorite.viewmodel.FavoriteViewModel
import com.itigradteamsix.snapshop.favorite.viewmodel.FavoriteViewModelFactory
import com.itigradteamsix.snapshop.home.view.SearchAdapter
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AddressFragment : Fragment() , OnDeleteListener{
    private lateinit var binding: FragmentAddressBinding
    private lateinit var adapter: AddressAdapter
    private lateinit var viewModel: AddressViewModel
    private var customerId: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addressViewModelFactory = AddressViewModelFactory(
            Repository.getInstance(ApiClient, ConcreteLocalSource(requireContext()))
        )

        viewModel = ViewModelProvider(
            this,
            addressViewModelFactory
        )[AddressViewModel::class.java]

        viewLifecycleOwner.lifecycleScope.launch {
            MyApplication.appInstance.settingsStore.userPreferencesFlow.collectLatest {
                customerId = it.customerId
                viewModel.getALlAddresses(customerId.toString(), requireContext())
            }
        }
        binding.rv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = AddressAdapter(requireContext(),this,customerId)
        binding.rv.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.addressFlow.collect { result ->
                when (result) {

                    is ApiState.Loading -> {
                    }

                    is ApiState.Success<*> -> {

                        val addressList =
                            result.data as ArrayList<com.itigradteamsix.snapshop.data.models.Address>
                        Log.d("Address List", addressList.toString())

                        adapter.submitList(addressList)

                    }

                    is ApiState.Failure -> {
                        Log.d("FavDraftFlowCollect", result.msg)
                    }
                }

            }
        }
//        viewLifecycleOwner.lifecycleScope.launch {
//
//            viewModel.newAddressFlow.collect { result ->
//                when (result) {
//
//                    is ApiState.Loading -> {
//                    }
//
//                    is ApiState.Success<*> -> {
//
//
//
//                    }
//
//                    is ApiState.Failure -> {
//                    }
//                }
//
//            }
//        }
        binding.button.setOnClickListener {

            val action =
                AddressFragmentDirections.actionAddressFragmentToMapFragment(customerId)
            binding.root.findNavController().navigate(action)
        }
        }

    @SuppressLint("NotifyDataSetChanged")
    override fun onAddressRemove(customerId: Long, context: Context, addressId: Long) {
        viewModel.removeAddress(addressId.toString(),customerId.toString(),context)
        viewModel.getALlAddresses(customerId.toString(),context)
        adapter.notifyDataSetChanged()
    }

    override fun onDefaultAddress(customerId: Long, context: Context, addressId: Long) {
        viewModel.makeAddressDefault(customerId.toString(), addressId.toString())
        viewModel.getALlAddresses(customerId.toString(),context)
        adapter.notifyDataSetChanged()

    }


}

