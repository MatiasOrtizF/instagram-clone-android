package com.mfo.instagramclone.data.network.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class PostResponse (
    @SerializedName ("id") val id: Long,
    @SerializedName ("image") val image: String,
    @SerializedName ("content") val content: String,
    @SerializedName ("likes") val likes: Int,
    @SerializedName ("comments") val comments: Int,
    @SerializedName ("createdAt") val createdAt: LocalDate
) {
    fun toDomain(): PostResponse {
        return PostResponse(
            id = id,
            image = image,
            content = content,
            likes = likes,
            comments = comments,
            createdAt = createdAt
        )
    }
}