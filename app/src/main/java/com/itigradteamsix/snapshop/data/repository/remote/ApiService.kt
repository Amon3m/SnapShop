package com.itigradteamsix.snapshop.data.repository.remote

import com.itigradteamsix.snapshop.data.models.Product
import com.itigradteamsix.snapshop.data.models.ProductListResponse
import com.itigradteamsix.snapshop.data.models.SmartCollectionResponse
import com.itigradteamsix.snapshop.data.models.SmartCollectionsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //https://shopify.dev/docs/api/admin-rest/2023-07/resources/product#get-products
    //Retrieve list of all products ✅
    @GET("products.json")
    suspend fun getAllProducts(): ProductListResponse

    //Retrieve a number of products ✅
    @GET("products.json")
    suspend fun getANumberOfProducts(@Query("limit") number: Int): ProductListResponse

    //Retrieve all products that belong to a certain collection ✅
    @GET("products.json")
    suspend fun getProductsByCollectionId(@Query("collection_id") id: Long): ProductListResponse

    //these are all the brands ✅
    @GET("smart_collections.json")
    suspend fun getSmartCollections(): SmartCollectionsResponse

    //get Only one smart collection (brand) by id
    @GET("smart_collections/{id}.json")
    suspend fun getSmartCollectionById(@Path("id") id: Long): SmartCollectionResponse



}