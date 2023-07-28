package com.itigradteamsix.snapshop.authentication.signup.model

import com.itigradteamsix.snapshop.favorite.model.DraftOrder

sealed class APiDraftState{
    data class Success(val data: DraftOrder?) : APiDraftState()
    data class Failure(val exception: Exception) : APiDraftState()
    object Loading : APiDraftState()
}
