package com.mfo.instagramclone.domain.usecase.comment

import com.mfo.instagramclone.data.network.response.CommentResponse
import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class PostCommentUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String, postId: Long, comment: String): CommentResponse? = repository.addComment(token, postId, comment)
}