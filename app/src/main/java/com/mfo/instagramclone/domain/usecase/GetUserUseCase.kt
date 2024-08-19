package com.mfo.instagramclone.domain.usecase

import com.mfo.instagramclone.data.network.response.UserResponse
import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String, userId: Long): UserResponse? {
        val user: UserResponse? = repository.getUser(token, userId)
        if(token.isNotEmpty()) {
            val followed =  repository.getFollowedUser(token, userId)
            user?.followed = followed
        }
        return user
    }
}