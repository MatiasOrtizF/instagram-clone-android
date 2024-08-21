package com.mfo.instagramclone.domain.usecase.follow

import com.mfo.instagramclone.data.network.response.FollowResponse
import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class GetFollowingsUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String, userId: Long): List<FollowResponse>? = repository.getAllFollowing(token, userId)
}