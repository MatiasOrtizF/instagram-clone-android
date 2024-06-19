package com.mfo.instagramclone.domain

import com.mfo.instagramclone.data.network.response.LoginResponse
import com.mfo.instagramclone.data.network.response.UserResponse
import com.mfo.instagramclone.domain.models.LoginRequest

interface Repository {
    // login
    suspend fun authenticationUser(loginRequest: LoginRequest): LoginResponse?

    // user
    suspend fun getUserInfo(token: String): UserResponse?
}