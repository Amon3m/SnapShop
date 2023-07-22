package com.itigradteamsix.snapshop.brands.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.brands.viewmodel.BrandsViewModel
import com.itigradteamsix.snapshop.brands.viewmodel.BrandsViewModelFactory
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentBrandsBinding
import com.itigradteamsix.snapshop.home.viewmodel.HomeViewModel
import com.itigradteamsix.snapshop.home.viewmodel.HomeViewModelFactory
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.model.SmartCollectionsItem
import com.itigradteamsix.snapshop.model.SmartCollectionsResponse
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.launch


class BrandsFragment : Fragment() ,OnBrandsClickListener{
    lateinit var binding: FragmentBrandsBinding
    lateinit var brandsAdapter: BrandsAdapter
    lateinit var brandsViewModel: BrandsViewModel
    lateinit var brandsViewModelFactory: BrandsViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        brandsViewModelFactory = BrandsViewModelFactory(
            Repository.getInstance(
                ApiClient,
                ConcreteLocalSource(requireContext())
            )
        )

        brandsViewModel = ViewModelProvider(this, brandsViewModelFactory).get(BrandsViewModel::class.java)
        brandsViewModel.getSmartCollections(requireContext())


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentBrandsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        brandsAdapter = BrandsAdapter(requireContext(),this)

        binding.brandRecycler.apply {
            adapter = brandsAdapter
        }
        lifecycleScope.launch {
            brandsViewModel.brands.collect {
                when (it) {
                    is ApiState.Success<*> -> {

                        val data = it.data as? SmartCollectionsResponse

                        val brands=data?.smartCollections
                        brandsAdapter.submitList(brands)

                        Log.e("src","${brands?.get(0)?.title}")

                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()

                    }
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                        // progressBar.visibility = View.GONE
                    }

                    else -> {

                        Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

    }

    override fun onBrandClick(smartCollectionsItem: SmartCollectionsItem?) {
        if  (smartCollectionsItem?.id != null) {
            val action=BrandsFragmentDirections.actionBrandsFragmentToProductsFragment(smartCollectionsItem.id)
            Navigation.findNavController(requireView()).navigate(action)
            }
    }

}