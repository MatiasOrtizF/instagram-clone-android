package com.mfo.instagramclone.ui.main

import com.mfo.instagramclone.data.network.response.UserResponse
import com.mfo.instagramclone.domain.models.User

sealed class MainState {
    data object Loading: MainState()
    data class Error(val error: String): MainState()
    data class Success(val user: UserResponse): MainState()
}