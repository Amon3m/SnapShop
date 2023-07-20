package com.itigradteamsix.snapshop.network

import com.itigradteamsix.snapshop.model.ProductListResponse
import com.itigradteamsix.snapshop.model.SmartCollectionResponse
import com.itigradteamsix.snapshop.model.SmartCollectionsResponse

sealed class ApiStateProductList{
    class Success(val data : ProductListResponse) : ApiStateProductList()
    class Failure(val msg: String) : ApiStateProductList()
    object Loading : ApiStateProductList()
}
sealed class ApiStateSmartCollection{
    class Success(val data : SmartCollectionResponse) : ApiStateSmartCollection()
    class Failure(val msg: String) : ApiStateSmartCollection()
    object Loading : ApiStateSmartCollection()
}
