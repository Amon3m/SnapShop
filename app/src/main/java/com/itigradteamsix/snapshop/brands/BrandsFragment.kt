package com.itigradteamsix.snapshop.brands

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.databinding.FragmentBrandsBinding


class BrandsFragment : Fragment() {
    lateinit var binding: FragmentBrandsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentBrandsBinding.inflate(inflater, container, false)
        return binding.root
    }

}