package com.itigradteamsix.snapshop.data.repository.local

import kotlinx.coroutines.flow.Flow

interface LocalSource {
    suspend fun getSomeListFromDatabase(): Flow<List<String>>
}