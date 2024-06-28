package com.mfo.instagramclone.domain.usecase

import com.mfo.instagramclone.data.network.response.UserSearchResponse
import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class GetUserSearchByUserNameUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String, word: String): List<UserSearchResponse>? = repository.getUserByUserName(token, word)
}