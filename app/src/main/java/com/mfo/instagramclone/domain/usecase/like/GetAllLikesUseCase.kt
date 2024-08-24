package com.mfo.instagramclone.domain.usecase.like

import com.mfo.instagramclone.data.network.response.PostActionResponse
import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class GetAllLikesUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String): List<PostActionResponse>? = repository.getAllMyLikes(token)
}