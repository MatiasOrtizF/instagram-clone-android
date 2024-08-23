package com.mfo.instagramclone.domain.usecase.save

import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class AddSavePostUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke (token: String, postId: Long): Map<String, Boolean>? = repository.addSave(token, postId)
}