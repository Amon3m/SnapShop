package com.itigradteamsix.snapshop.model

import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface RepoInterface {
    suspend fun getAllProducts(): Flow<ProductListResponse>

    suspend fun getANumberOfProducts(limit: Int): Flow<ProductListResponse>
    suspend fun getProductsByCollectionId(id: Long): Flow<ProductListResponse>

    suspend fun getSmartCollectionById(id: Long): Flow<SmartCollectionResponse>
    suspend fun getSmartCollections(): Flow<SmartCollectionsResponse>


    suspend fun getSomeListFromDatabase(): Flow<List<String>>
    suspend fun updateDraftOrder(draftOrderId : Long , draftResponse: DraftOrderResponse): Flow<DraftOrder?>

}
