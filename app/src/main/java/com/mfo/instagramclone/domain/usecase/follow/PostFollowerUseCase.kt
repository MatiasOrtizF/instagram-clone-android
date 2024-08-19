package com.mfo.instagramclone.domain.usecase.follow

import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class PostFollowerUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String, userId: Long): Map<String, Boolean>? = repository.addFollower(token, userId)
}