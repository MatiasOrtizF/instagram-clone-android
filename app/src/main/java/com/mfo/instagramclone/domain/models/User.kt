package com.mfo.instagramclone.domain.models


class User (
    val name: String,
    val lastName: String,
    val email: String,
    val userName: String,
    val verified: Boolean,
    val imageProfile: String?,
    val description: String?,
    val link: String?,
    val numberPost: Long,
    val numberFollowers: Long,
    val numberFollowing: Long
    ) {
}