package com.itigradteamsix.snapshop.authentication

import com.itigradteamsix.snapshop.favorite.model.DraftOrder

sealed class ApiDraftLoginState{
    data class Success(val data: DraftOrder?) : ApiDraftLoginState()
    data class Failure(val exception: Exception) : ApiDraftLoginState()
    object Loading : ApiDraftLoginState()
}

