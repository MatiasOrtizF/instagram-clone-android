package com.mfo.instagramclone.domain

import com.mfo.instagramclone.data.network.response.CommentResponse
import com.mfo.instagramclone.data.network.response.LoginResponse
import com.mfo.instagramclone.data.network.response.PostResponse
import com.mfo.instagramclone.data.network.response.UserResponse
import com.mfo.instagramclone.data.network.response.UserSearchResponse
import com.mfo.instagramclone.domain.models.LoginRequest

interface Repository {
    // login
    suspend fun authenticationUser(loginRequest: LoginRequest): LoginResponse?

    // user
    suspend fun getUserInfo(token: String): UserResponse?

    suspend fun getUserByUserName(token: String, word: String): List<UserSearchResponse>?

    // post
    suspend fun getPost(postId: Long): PostResponse?

    // comment
    suspend fun getComments(token: String, postId: Long): List<CommentResponse>?

    // like
    suspend fun getLikedPost(token: String, postId: Long): Boolean?
    suspend fun addLike(token: String, postId: Long): Map<String, Boolean>?
    suspend fun deleteLike(token: String, postId: Long): Map<String, Boolean>?

    // save
    suspend fun getSavedPost(token: String, postId: Long): Boolean?
}