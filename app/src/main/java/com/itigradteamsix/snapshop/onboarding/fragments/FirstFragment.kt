package com.itigradteamsix.snapshop.onboarding.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2
import com.itigradteamsix.snapshop.MainActivity
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {
    lateinit var binding: FragmentFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentFirstBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager=activity?.findViewById<ViewPager2>(R.id.view_pager)
        binding.button3.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_viewPagerFragment2_to_loginFragment2)

        }
        binding.next.setOnClickListener {
            viewPager?.currentItem=1
        }
    }

}