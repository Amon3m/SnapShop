package com.itigradteamsix.snapshop.database

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class ConcreteLocalSource(context: Context): LocalSource {

    companion object {
        private var concreteLocalSource: ConcreteLocalSource? = null

        fun getInstance(context: Context): ConcreteLocalSource {
            if (concreteLocalSource == null) {
                concreteLocalSource = ConcreteLocalSource(context)
            }
            return concreteLocalSource!!
        }
    }



    override suspend fun getSomeListFromDatabase(): Flow<List<String>> {
//        TODO("Not yet implemented")
        return emptyFlow()
    }


}