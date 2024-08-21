package com.mfo.instagramclone.ui.follow

import com.mfo.instagramclone.data.network.response.FollowResponse

sealed class FollowState {
    data object Loading : FollowState()
    data class Error(val error: String) : FollowState()
    data class Success(val users: List<FollowResponse>) : FollowState()
    data class FollowSuccess(val success: Map<String, Boolean>?): FollowState()
}