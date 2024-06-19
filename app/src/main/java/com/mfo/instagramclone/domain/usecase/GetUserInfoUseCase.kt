package com.mfo.instagramclone.domain.usecase

import com.mfo.instagramclone.data.network.response.UserResponse
import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String): UserResponse? = repository.getUserInfo(token)
}