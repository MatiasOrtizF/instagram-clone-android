package com.mfo.instagramclone.data

import android.util.Log
import com.mfo.instagramclone.data.network.InstagramCloneApiService
import com.mfo.instagramclone.data.network.response.LoginResponse
import com.mfo.instagramclone.data.network.response.PostResponse
import com.mfo.instagramclone.data.network.response.UserResponse
import com.mfo.instagramclone.data.network.response.UserSearchResponse
import com.mfo.instagramclone.domain.Repository
import com.mfo.instagramclone.domain.models.LoginRequest
import retrofit2.HttpException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: InstagramCloneApiService): Repository {
    // login
    override suspend fun authenticationUser(loginRequest: LoginRequest): LoginResponse? {
        runCatching { apiService.authenticationUser(loginRequest)}
            .onSuccess { return it.toDomain() }
            .onFailure { throwable ->
                val errorMessage = when (throwable) {
                    is HttpException -> throwable.response()?.errorBody()?.string()
                    else -> null
                } ?: "An error occurred: ${throwable.message}"
                Log.i("mfo", "Error occurred: $errorMessage")
                throw Exception(errorMessage)
            }
        return null
    }

    // user
    override suspend fun getUserInfo(token: String): UserResponse? {
        runCatching { apiService.getUserInfo(token)}
            .onSuccess { return it.toDomain() }
            .onFailure { throwable ->
                val errorMessage = when (throwable) {
                    is HttpException -> throwable.response()?.errorBody()?.string()
                    else -> null
                } ?: "An error occurred: ${throwable.message}"
                Log.i("mfo", "Error occurred: $errorMessage")
                throw Exception(errorMessage)
            }
        return null
    }

    override suspend fun getUserByUserName(token: String, word: String): List<UserSearchResponse>? {
        runCatching {
            val appointments = apiService.getUserByUserName(token, word)
            appointments.map {
                it.toDomain()
            }
        }
            .onSuccess { appointments -> return appointments }
            .onFailure { throwable ->
                val errorMessage = when (throwable) {
                    is HttpException -> throwable.response()?.errorBody()?.string()
                    else -> null
                } ?: "An error occurred: ${throwable.message}"
                Log.i("mfo", "Error occurred: $errorMessage")
                throw Exception(errorMessage)
            }
        return null
    }

    // post
    override suspend fun getPost(postId: Long): PostResponse? {
        runCatching { apiService.getPost(postId) }
            .onSuccess { return it.toDomain() }
            .onFailure { throwable ->
                val errorMessage = when (throwable) {
                    is HttpException -> throwable.response()?.errorBody()?.string()
                    else -> null
                } ?: "An error occurred: ${throwable.message}"
                Log.i("mfo", "Error occurred: $errorMessage")
                throw Exception(errorMessage)
            }
        return null
    }
}