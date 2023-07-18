package com.itigradteamsix.snapshop.data.repository

import com.itigradteamsix.snapshop.data.models.Product
import com.itigradteamsix.snapshop.data.repository.local.LocalSource
import com.itigradteamsix.snapshop.data.repository.remote.ShopifyApiClient

class ShopRepository(private val localSource: LocalSource) {

    companion object{
        private val repository: ShopRepository? = null

        fun getInstance(localSource: LocalSource): ShopRepository {
            if (repository == null) {
                return ShopRepository(localSource)
            }
            return repository
        }
    }

    //Remote methods
    suspend fun getAllProducts():List<Product>? {
        return ShopifyApiClient.getAllProducts()
    }

    suspend fun getANumberOfProducts(limit: Int):List<Product>? {
        return ShopifyApiClient.getANumberOfProducts(limit)
    }

    suspend fun getProductsByCollectionId(id: Long):List<Product>? {
        return ShopifyApiClient.getProductsByCollectionId(id)
    }

    suspend fun getSmartCollectionById(id: Long) = ShopifyApiClient.getSmartCollectionById(id)

    suspend fun getSmartCollections() = ShopifyApiClient.getSmartCollections()


    //Local methods
    suspend fun getSomeListFromDatabase() = localSource.getSomeListFromDatabase()




}