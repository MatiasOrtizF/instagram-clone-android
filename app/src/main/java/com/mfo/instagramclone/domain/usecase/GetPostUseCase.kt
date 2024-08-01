package com.mfo.instagramclone.domain.usecase

import com.mfo.instagramclone.data.network.response.PostResponse
import com.mfo.instagramclone.domain.Repository
import javax.inject.Inject

class GetPostUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(token: String, postId: Long): PostResponse? {
        val post: PostResponse? =  repository.getPost(postId)
        if(token.isNotEmpty()) {
            val liked =  repository.getLikedPost(token, postId)
            post?.liked = liked

            val saved = repository.getSavedPost(token, postId)
            post?.saved = saved
        }
        return post
    }
}