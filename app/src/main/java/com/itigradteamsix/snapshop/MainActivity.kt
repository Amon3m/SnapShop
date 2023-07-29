package com.itigradteamsix.snapshop

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.itigradteamsix.snapshop.databinding.ActivityMainBinding
import com.itigradteamsix.snapshop.home.view.HomeFragment
import com.stripe.android.paymentsheet.PaymentSheet

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        setSupportActionBar(binding.topAppBar)



        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.shoppingCartFragment -> {
                    binding.topAppBar.title = getString(R.string.cart)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false);
                    supportActionBar?.setDisplayShowHomeEnabled(false);
                    binding.bottomNavigationView.visibility= VISIBLE

                }
                R.id.homeFragment -> {
                    binding.topAppBar.title = getString(R.string.home)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false);
                    supportActionBar?.setDisplayShowHomeEnabled(false);
                    binding.bottomNavigationView.visibility= VISIBLE

                }
                R.id.profileFragment -> {
                    binding.topAppBar.title = getString(R.string.profile)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false);
                    supportActionBar?.setDisplayShowHomeEnabled(false);
                    binding.bottomNavigationView.visibility=VISIBLE

                }


                R.id.wishlistFragment -> {
                    binding.topAppBar.title = getString(R.string.wishlist)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false);
                    supportActionBar?.setDisplayShowHomeEnabled(false);
                    binding.bottomNavigationView.visibility=VISIBLE

                }

                else -> {
                    binding.topAppBar.title = getString(R.string.app_name)
                    supportActionBar?.setDisplayHomeAsUpEnabled(true);
                    supportActionBar?.setDisplayShowHomeEnabled(true);
                    binding.bottomNavigationView.visibility=GONE
                }
            }

        }


    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        navController.navigate(R.id.homeFragment)

    }
}