package com.mfo.instagramclone.data.network

import com.mfo.instagramclone.data.network.response.LoginResponse
import com.mfo.instagramclone.data.network.response.PostResponse
import com.mfo.instagramclone.data.network.response.UserResponse
import com.mfo.instagramclone.data.network.response.UserSearchResponse
import com.mfo.instagramclone.domain.models.LoginRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface InstagramCloneApiService {

    // login
    @POST("login")
    suspend fun authenticationUser(@Body loginRequest: LoginRequest): LoginResponse

    // user
    @GET("user")
    suspend fun getUserInfo(@Header ("Authorization") authorization: String): UserResponse

    @GET("user/search")
    suspend fun getUserByUserName(
        @Header ("Authorization") authorization: String,
        @Query ("word") word: String
    ): List<UserSearchResponse>

    // post
    @GET("post/{id}")
    suspend fun getPost(@Path ("id") postId: Long): PostResponse
}