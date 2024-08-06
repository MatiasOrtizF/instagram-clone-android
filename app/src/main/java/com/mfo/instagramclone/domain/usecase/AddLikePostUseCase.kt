package com.mfo.instagramclone.domain.usecase

import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class AddLikePostUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String, postId: Long): Map<String, Boolean>? = repository.addLike(token, postId)
}