package com.mfo.instagramclone.data.network.response

import com.google.gson.annotations.SerializedName

data class CommentResponse (
    @SerializedName("id") val id: Long,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("likes") var likes: Int,
    @SerializedName("liked") var liked: Boolean,
    @SerializedName("user") val user: UserComment
) {

    fun toDomain(): CommentResponse {
        return CommentResponse(
            id= id,
            content = content,
            createdAt = createdAt,
            likes = likes,
            liked = liked,
            user = user
        )
    }

    data class UserComment (
        val id: Long,
        val imageProfile: String?,
        val userName: String,
        val name: String,
        val lastName: String,
        val verified: Boolean
    )
}