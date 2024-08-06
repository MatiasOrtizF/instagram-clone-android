package com.mfo.instagramclone.domain.usecase

import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class DeleteLikePostUseCase @Inject constructor(private val repository: Repository)  {
    suspend operator fun invoke(token: String, postId: Long): Map<String, Boolean>? = repository.deleteLike(token, postId)
}