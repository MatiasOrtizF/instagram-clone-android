package com.mfo.instagramclone.domain.usecase

import com.mfo.instagramclone.data.network.response.LoginResponse
import com.mfo.instagramclone.domain.Repository
import com.mfo.instagramclone.domain.models.LoginRequest
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(loginRequest: LoginRequest): LoginResponse? = repository.authenticationUser(loginRequest)
}