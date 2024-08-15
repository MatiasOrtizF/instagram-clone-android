package com.mfo.instagramclone.data.network.response

import com.google.gson.annotations.SerializedName

class UserHistoryResponse (
    @SerializedName("historyId") val id: Long?,
    @SerializedName("id") val userId: Long,
    @SerializedName("imageProfile") val imageProfile: String?,
    @SerializedName("userName") val userName: String,
    @SerializedName("name") val name: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("verified") val verified: Boolean
) {

    fun toDomain(): UserHistoryResponse {
        return UserHistoryResponse(
            id = id,
            userId = userId,
            imageProfile = imageProfile,
            userName = userName,
            name = name,
            lastName = lastName,
            verified = verified
        )
    }
}