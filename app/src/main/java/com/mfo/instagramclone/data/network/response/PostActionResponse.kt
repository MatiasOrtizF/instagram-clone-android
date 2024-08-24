package com.mfo.instagramclone.data.network.response

import com.google.gson.annotations.SerializedName

data class PostActionResponse (
    @SerializedName("id") val id: Long,
    @SerializedName("image") val img: String
) {
    fun toDomain(): PostActionResponse {
        return PostActionResponse(
            id = id,
            img = img
        )
    }
}