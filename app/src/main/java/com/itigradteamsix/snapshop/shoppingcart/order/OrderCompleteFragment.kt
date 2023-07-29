package com.itigradteamsix.snapshop.shoppingcart.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.databinding.FragmentOrderCompleteBinding


class OrderCompleteFragment : Fragment() {

    private lateinit var binding: FragmentOrderCompleteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentOrderCompleteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backToHome.setOnClickListener {

            val action =
                OrderCompleteFragmentDirections.actionOrderCompleteFragmentToHomeFragment()
            findNavController().navigate(action)

        }
    }


}