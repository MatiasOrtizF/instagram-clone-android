package com.mfo.instagramclone.ui.splash

import com.mfo.instagramclone.data.network.response.UserResponse

sealed class SplashState {
    data object Loading: SplashState()
    data class Error(val error: String): SplashState()
    data class Success(val user: UserResponse): SplashState()
}