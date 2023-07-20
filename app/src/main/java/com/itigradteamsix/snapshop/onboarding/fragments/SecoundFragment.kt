package com.itigradteamsix.snapshop.onboarding.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.itigradteamsix.snapshop.MainActivity
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.databinding.FragmentSecoundBinding
import com.itigradteamsix.snapshop.databinding.FragmentViewPagerBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SecoundFragment : Fragment() {
lateinit var binding: FragmentSecoundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSecoundBinding.inflate(inflater,container,false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager=activity?.findViewById<ViewPager2>(R.id.view_pager)
        binding.button3.setOnClickListener {
//            val intent = Intent(activity, MainActivity::class.java)
//            startActivity(intent)
//            activity?.finish()
            Navigation.findNavController(requireView())
                .navigate(R.id.action_viewPagerFragment2_to_loginFragment2)
        }
        binding.nextSec.setOnClickListener {
            viewPager?.currentItem=3
        }
        binding.backSec.setOnClickListener {
            viewPager?.currentItem=0
        }
    }

}