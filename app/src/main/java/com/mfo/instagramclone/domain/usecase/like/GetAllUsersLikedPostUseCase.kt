package com.mfo.instagramclone.domain.usecase.like

import com.mfo.instagramclone.data.network.response.FollowResponse
import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class GetAllUsersLikedPostUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String, postId: Long): List<FollowResponse>? = repository.getAllUsersLikedPost(token, postId)
}