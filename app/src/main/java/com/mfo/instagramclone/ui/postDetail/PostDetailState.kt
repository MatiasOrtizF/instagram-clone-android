package com.mfo.instagramclone.ui.postDetail

import com.mfo.instagramclone.data.network.response.PostResponse


sealed class PostDetailState {
    data object Loading: PostDetailState()
    data class Error(val error: String): PostDetailState()
    data class Success(val post: PostResponse): PostDetailState()
}