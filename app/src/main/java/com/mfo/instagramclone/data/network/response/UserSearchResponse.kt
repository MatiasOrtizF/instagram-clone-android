package com.mfo.instagramclone.data.network.response

import com.google.gson.annotations.SerializedName

data class UserSearchResponse (
    @SerializedName("id") val id: Long,
    @SerializedName("imageProfile") val imageProfile: String?,
    @SerializedName("userName") val userName: String,
    @SerializedName("name") val name: String,
    @SerializedName("lastName") val lastName: String
) {

    fun toDomain(): UserSearchResponse {
        return UserSearchResponse(
            id = id,
            imageProfile = imageProfile,
            userName = userName,
            name = name,
            lastName = lastName
        )
    }
}