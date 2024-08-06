package com.mfo.instagramclone.data.network.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName ("token") val token: String,
    @SerializedName ("imageProfile") val imageProfile: String?
    ) {

    fun toDomain(): LoginResponse {
        return LoginResponse(
            token = token,
            imageProfile = imageProfile
        )
    }
}