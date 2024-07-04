package com.mfo.instagramclone.data.network.response

import com.google.gson.annotations.SerializedName
import com.mfo.instagramclone.domain.models.User

data class LoginResponse(
    @SerializedName ("token") val token: String,
    @SerializedName ("user") val user: User
    ) {

    fun toDomain(): LoginResponse {
        return LoginResponse(
            token = token,
            user = user
        )
    }
}