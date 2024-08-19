package com.mfo.instagramclone.ui.userProfile

import com.mfo.instagramclone.data.network.response.UserResponse

sealed class UserProfileState {
    data object Loading: UserProfileState()
    data class Error(val error: String): UserProfileState()
    data class Success(val user: UserResponse): UserProfileState()
    data class FollowSuccess(val success: Map<String, Boolean>?): UserProfileState()
}