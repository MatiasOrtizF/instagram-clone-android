package com.mfo.instagramclone.domain.usecase

import com.mfo.instagramclone.data.network.response.CommentResponse
import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(private val repository: Repository)  {
    suspend operator fun invoke(token: String, postId: Long): List<CommentResponse>? = repository.getComments(token, postId)
}