package com.itigradteamsix.snapshop.authentication

import com.itigradteamsix.snapshop.favorite.model.DraftOrder
import com.itigradteamsix.snapshop.favorite.model.DraftOrderResponse

sealed class APiDraftState{
    data class Success(val data: DraftOrder?) : APiDraftState()
    data class Failure(val exception: Exception) : APiDraftState()
    object Loading : APiDraftState()
}
