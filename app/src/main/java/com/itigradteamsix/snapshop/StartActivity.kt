package com.itigradteamsix.snapshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.itigradteamsix.snapshop.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class StartActivity : AppCompatActivity() {

//    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.Main) {
            MyApplication.appInstance.settingsStore.userPreferencesFlow.collect{
                if(it.isLoggedIn){
                    val intent = Intent(this@StartActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    setContentView(R.layout.activity_start)
//                    navController = Navigation.findNavController(this@StartActivity, R.id.fragmentContainerView)
//                    if (!it.isFirstTime){
//                        navController.navigate(R.id.action_viewPagerFragment2_to_loginFragment2)
//                    }
                }
            }
        }

    }
}

