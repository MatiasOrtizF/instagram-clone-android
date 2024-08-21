package com.mfo.instagramclone.data.network.response

import com.google.gson.annotations.SerializedName

data class FollowResponse (
    @SerializedName("id") val id: Long,
    @SerializedName("imageProfile") val imageProfile: String?,
    @SerializedName("userName") val userName: String,
    @SerializedName("name") val name: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("verified") val verified: Boolean,
    @SerializedName("followed") var followed: Boolean
){

    fun toDomain(): FollowResponse {
        return FollowResponse(
            id = id,
            imageProfile = imageProfile,
            userName = userName,
            name = name,
            lastName = lastName,
            verified = verified,
            followed = followed
        )
    }
}