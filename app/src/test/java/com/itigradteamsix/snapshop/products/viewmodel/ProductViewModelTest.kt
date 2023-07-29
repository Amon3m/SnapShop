package com.itigradteamsix.snapshop.products.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myweather.util.MainDispatcherRule
import com.itigradteamsix.snapshop.repo.FakeRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ProductViewModel
    private lateinit var fakeRepo: FakeRepo
    @Before
    fun setUp() {
        fakeRepo = FakeRepo()
        viewModel = ProductViewModel(fakeRepo)
    }
    @Test
    fun getProducts() {
    }
}