package com.itigradteamsix.snapshop.database

import kotlinx.coroutines.flow.Flow

interface LocalSource {
    suspend fun getSomeListFromDatabase(): Flow<List<String>>
}