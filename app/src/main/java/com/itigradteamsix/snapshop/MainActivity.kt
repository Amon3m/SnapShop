package com.itigradteamsix.snapshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.itigradteamsix.snapshop.databinding.ActivityMainBinding

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
        setSupportActionBar(binding.topAppBar)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.shoppingCartFragment -> {
                    binding.topAppBar.title = getString(R.string.shopping_cart)
                }
                R.id.settingsFragment -> {
                    binding.topAppBar.title = getString(R.string.settings)
                }
                else -> {
                    binding.topAppBar.title = getString(R.string.app_name)
                }
            }
        }

    }
}