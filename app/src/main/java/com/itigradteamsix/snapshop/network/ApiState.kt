package com.itigradteamsix.snapshop.network

import com.itigradteamsix.snapshop.model.ProductListResponse
import com.itigradteamsix.snapshop.model.SmartCollectionResponse
import com.itigradteamsix.snapshop.model.SmartCollectionsResponse

sealed class ApiState{
    class Success<T>(val data : T) : ApiState()
    class Failure(val msg: String) : ApiState()
    object Loading : ApiState()
}

