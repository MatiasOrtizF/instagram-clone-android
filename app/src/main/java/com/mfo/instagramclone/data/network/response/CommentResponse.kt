package com.mfo.instagramclone.data.network.response

import com.google.gson.annotations.SerializedName

data class CommentResponse (
    @SerializedName("id") val id: Long,
    @SerializedName("content") val content: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("likes") val likes: Int,
    @SerializedName("user") val user: PostResponse.PostUser
) {

    fun toDomain(): CommentResponse {
        return CommentResponse(
            id= id,
            content = content,
            createdAt = createdAt,
            likes = likes,
            user = user
        )
    }
}