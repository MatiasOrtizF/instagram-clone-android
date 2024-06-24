package com.mfo.instagramclone.domain.usecase

import com.mfo.instagramclone.data.network.response.PostResponse
import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class GetPostUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(postId: Long): PostResponse? = repository.getPost(postId)
}