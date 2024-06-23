package com.mfo.instagramclone.ui.profile

import com.mfo.instagramclone.data.network.response.UserResponse

sealed class ProfileState {
    data object Loading: ProfileState()
    data class Error(val error: String): ProfileState()
    data class Success(val user: UserResponse): ProfileState()
}