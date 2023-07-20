package com.itigradteamsix.snapshop.model

import com.itigradteamsix.snapshop.database.LocalSource
import com.itigradteamsix.snapshop.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class Repository private constructor(
    var remoteSource: RemoteSource, var concreteLocalSource: LocalSource
) : RepoInterface {

    companion object {
        private var instance: Repository? = null
        fun getInstance(
            remoteSource: RemoteSource,
            concreteLocalSource: LocalSource
        ): Repository {
            return instance ?: synchronized(this) {
                val temp = Repository(remoteSource, concreteLocalSource)
                instance = temp
                temp
            }
        }
    }



    override suspend fun getAllProducts():Flow<ProductListResponse> {
        return flowOf(remoteSource.getAllProducts())
    }

    override suspend fun getANumberOfProducts(limit: Int):Flow<ProductListResponse> {
        return flowOf(remoteSource.getANumberOfProducts(limit))
    }
    override suspend fun getProductsByCollectionId(id: Long):Flow<ProductListResponse> {
        return flowOf(remoteSource.getProductsByCollectionId(id))
    }

    override suspend fun getSmartCollectionById(id: Long): Flow<SmartCollectionResponse> {
        return flowOf(remoteSource.getSmartCollectionById(id))
    }
    override suspend fun getSmartCollections(): Flow<SmartCollectionsResponse> {
        return flowOf(remoteSource.getSmartCollections())}


    override suspend fun getSomeListFromDatabase(): Flow<List<String>> {
        return concreteLocalSource.getSomeListFromDatabase()

    }



}