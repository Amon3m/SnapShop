package com.itigradteamsix.snapshop.network

import com.itigradteamsix.snapshop.model.Product
import com.itigradteamsix.snapshop.model.ProductListResponse
import com.itigradteamsix.snapshop.model.SmartCollection
import com.itigradteamsix.snapshop.model.SmartCollectionResponse
import com.itigradteamsix.snapshop.model.SmartCollectionsResponse

interface RemoteSource {

//    suspend fun getWeatherFromNetwork(
//        lat: Double=0.0,
//        lon: Double=0.0,
//        exclude:String="",
//        units:String="",
//        lang:String="",
//        appid:String="ccb811f49ff661e0a43e8d8727e0387a"
//    ): WeatherResponse
    suspend fun getAllProducts(): ProductListResponse
    suspend fun getProductsByCollectionId(collectionId: Long): ProductListResponse
    suspend fun getANumberOfProducts(number: Int): ProductListResponse
    suspend fun getSmartCollectionById(id: Long): SmartCollectionResponse
    suspend fun getSmartCollections(): SmartCollectionsResponse

}
