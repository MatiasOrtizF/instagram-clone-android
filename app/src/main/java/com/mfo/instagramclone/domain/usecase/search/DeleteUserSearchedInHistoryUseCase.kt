package com.mfo.instagramclone.domain.usecase.search

import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class DeleteUserSearchedInHistoryUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String, id: Long): Map<String,Boolean>? = repository.deleteUserSearchedInHistory(token, id)
}