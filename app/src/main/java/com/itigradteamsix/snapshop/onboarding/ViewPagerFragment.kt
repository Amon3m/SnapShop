package com.itigradteamsix.snapshop.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.databinding.FragmentProfileBinding
import com.itigradteamsix.snapshop.databinding.FragmentViewPagerBinding
import com.itigradteamsix.snapshop.onboarding.fragments.FirstFragment
import com.itigradteamsix.snapshop.onboarding.fragments.SecoundFragment
import com.itigradteamsix.snapshop.onboarding.fragments.ThirdFragment


class ViewPagerFragment : Fragment() {
lateinit var binding: FragmentViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentList= arrayListOf<Fragment>(
            FirstFragment(),
            SecoundFragment(),
            ThirdFragment()
        )

        val adapter=ViewPagerAdapter(fragmentList,
        requireActivity().supportFragmentManager,
        lifecycle)
        binding= FragmentViewPagerBinding.inflate(inflater,container,false)

        binding.viewPager.adapter=adapter

        return binding.root}

}