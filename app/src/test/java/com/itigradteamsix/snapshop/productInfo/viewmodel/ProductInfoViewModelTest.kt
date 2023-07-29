package com.itigradteamsix.snapshop.productInfo.viewmodel

import android.view.ContextMenu
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.myweather.util.MainDispatcherRule
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.model.Product
import com.itigradteamsix.snapshop.network.ApiState
import com.itigradteamsix.snapshop.products.viewmodel.ProductViewModel
import com.itigradteamsix.snapshop.repo.FakeRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.manipulation.Ordering.Context

class ProductInfoViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ProductInfoViewModel
    private lateinit var fakeRepo: FakeRepo

    @Test
    fun testSomethingWithContext() {
        // Get the application context
//        appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // Now you can use the appContext to access resources, databases, etc.
        // For example:
//        val appName = appContext.getString(R.string.app_name)

        // Perform your test assertions or actions that require the Context.
    }
    @Before
    fun setUp() {
          var appContext: android.content.Context= InstrumentationRegistry.getInstrumentation().targetContext

        fakeRepo = FakeRepo()
        viewModel = ProductInfoViewModel(fakeRepo, FirebaseRepo(FirebaseAuth.getInstance(FirebaseApp.initializeApp(appContext)!!)))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getSingleProduct() = runTest {
        viewModel.getSingleProduct(1,MyApplication.appContext)
        launch {
            viewModel.productFlow.collect{
                it as ApiState.Success<Product>
                assertThat(it.data!!.id, CoreMatchers.`is`(1))
                cancel()
            }
        }
    }

    @Test
    fun getGetDraftFlow() {
    }

    @Test
    fun updateDraftOrder() {
    }
}