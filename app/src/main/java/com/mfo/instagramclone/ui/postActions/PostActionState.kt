package com.mfo.instagramclone.ui.postActions

import com.mfo.instagramclone.data.network.response.PostActionResponse

sealed class PostActionState {
    data object Loading: PostActionState()
    data class Error(val error: String): PostActionState()
    data class Success(val post: List<PostActionResponse>): PostActionState()
}