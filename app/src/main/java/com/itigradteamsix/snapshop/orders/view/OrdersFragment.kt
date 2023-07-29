package com.itigradteamsix.snapshop.orders.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentOrdersBinding
import com.itigradteamsix.snapshop.model.OrderResponse
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.orders.viewmodel.OrdersViewModel
import com.itigradteamsix.snapshop.orders.viewmodel.OrdersViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class OrdersFragment : Fragment() {
    lateinit var binding: FragmentOrdersBinding
    lateinit var ordersViewModel: OrdersViewModel
    lateinit var ordersAdapter: OrdersAdapter
    lateinit var ordersViewModelFactory: OrdersViewModelFactory
    lateinit var userEmail: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ordersViewModelFactory = OrdersViewModelFactory(
            Repository.getInstance(
                ApiClient,
                ConcreteLocalSource(requireContext())
            )
        )

        ordersViewModel =
            ViewModelProvider(this, ordersViewModelFactory).get(OrdersViewModel::class.java)
        lifecycleScope.launch {
            MyApplication.appInstance.settingsStore.userPreferencesFlow.collectLatest {
                it.customerEmail.let { email ->
                    userEmail = email
                }
            }
        }
        ordersViewModel.getCustomerOrders(requireContext(), userEmail)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //change the title  to My Orders
        activity?.title = "My Orders"


        ordersAdapter = OrdersAdapter(requireContext())

        binding.orderRec.apply {

            adapter = ordersAdapter
        }

        lifecycleScope.launch {
            ordersViewModel.orders.collect {
                when (it) {
                    is ApiState.Success<*> -> {

                        val data = it.data as? OrderResponse

                        val orders = data?.orders
                        ordersAdapter.submitList(orders)

                        Log.e("testorder", "${orders?.get(0)?.email}")


                    }

                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                        // progressBar.visibility = View.GONE
                    }

                    else -> {

                    }
                }

            }
        }

    }

}

