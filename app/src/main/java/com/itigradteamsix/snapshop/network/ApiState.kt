package com.itigradteamsix.snapshop.network

import com.itigradteamsix.snapshop.model.ProductListResponse
import com.itigradteamsix.snapshop.model.SmartCollectionResponse
import com.itigradteamsix.snapshop.model.SmartCollectionsResponse

sealed class ApiState<T> {
    class Success<T>(val data : T) : ApiState<T>()
    class Failure(val msg: String) : ApiState<Any?>()
    object Loading : ApiState<Any?>()
}

