package com.mfo.instagramclone.domain.usecase.search

import com.mfo.instagramclone.data.network.response.UserHistoryResponse
import com.mfo.instagramclone.data.network.response.UserSearchResponse
import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class GetUsersSearchedHistoryUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String): List<UserHistoryResponse>? = repository.getUsersSearchedHistory(token)
}