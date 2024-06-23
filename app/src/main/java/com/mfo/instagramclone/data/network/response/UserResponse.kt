package com.mfo.instagramclone.data.network.response

import com.google.gson.annotations.SerializedName

data class UserResponse (
    @SerializedName("name") val name: String,
    @SerializedName ("lastName") val lastName: String,
    @SerializedName ("email") val email: String,
    @SerializedName ("userName") val userName: String,
    @SerializedName ("verified") val verified: Boolean,
    @SerializedName ("imageProfile") val imageProfile: String?,
    @SerializedName ("description") val description: String?,
    @SerializedName ("link") val link: String?,
    @SerializedName ("numberPost") val numberPost: Long,
    @SerializedName ("numberFollowers") val numberFollowers: Long,
    @SerializedName ("numberFollowing") val numberFollowing: Long,
    @SerializedName ("post") val post: List<UserPost>
) {
    fun toDomain(): UserResponse {
        return UserResponse(
            name = name,
            lastName = lastName,
            email = email,
            userName = userName,
            verified = verified,
            imageProfile = imageProfile,
            description = description,
            link = link,
            numberPost = numberPost,
            numberFollowers = numberFollowers,
            numberFollowing = numberFollowing,
            post = post
        )
    }

    data class UserPost (
        val id: Long,
        val image: String,
    )
}