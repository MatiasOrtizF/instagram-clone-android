package com.mfo.instagramclone.domain.usecase.like

import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class DeleteCommentLikeUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String, commentId: Long): Map<String, Boolean>? = repository.deleteCommentLike(token, commentId)
}