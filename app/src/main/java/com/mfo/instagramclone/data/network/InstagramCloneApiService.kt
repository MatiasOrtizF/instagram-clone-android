package com.mfo.instagramclone.data.network

import com.mfo.instagramclone.data.network.response.LoginResponse
import com.mfo.instagramclone.data.network.response.UserResponse
import com.mfo.instagramclone.domain.models.LoginRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface InstagramCloneApiService {

    // login
    @POST("login")
    suspend fun authenticationUser(@Body loginRequest: LoginRequest): LoginResponse

    // user
    @GET("user")
    suspend fun getUserInfo(@Header ("Authorization") authorization: String): UserResponse
}