package com.mfo.instagramclone.domain.usecase.search

import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class PostUserSearchedInHistoryUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String, userId: Long): Map<String,Boolean>? = repository.addUserSearchedInHistory(token, userId)
}