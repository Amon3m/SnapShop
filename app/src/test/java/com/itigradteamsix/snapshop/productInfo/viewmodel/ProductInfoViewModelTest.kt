package com.itigradteamsix.snapshop.productInfo.viewmodel

import android.view.ContextMenu
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
    @Before
    fun setUp() {
        fakeRepo = FakeRepo()
        viewModel = ProductInfoViewModel(fakeRepo, FirebaseRepo(FirebaseAuth.getInstance(FirebaseApp.initializeApp(MyApplication.appContext)!!)))
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