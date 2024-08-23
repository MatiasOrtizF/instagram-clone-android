package com.mfo.instagramclone.domain.usecase.save

import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class DeleteSavePostUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String, postId: Long): Map<String, Boolean>? = repository.deleteSave(token, postId)
}