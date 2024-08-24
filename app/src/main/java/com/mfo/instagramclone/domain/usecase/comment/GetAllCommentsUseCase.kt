package com.mfo.instagramclone.domain.usecase.comment

import com.mfo.instagramclone.data.network.response.PostActionResponse
import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class GetAllCommentsUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String): List<PostActionResponse>? = repository.getAllMyComments(token)
}