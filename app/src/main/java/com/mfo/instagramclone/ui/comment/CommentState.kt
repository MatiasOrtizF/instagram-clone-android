package com.mfo.instagramclone.ui.comment

import com.mfo.instagramclone.data.network.response.CommentResponse

sealed class CommentState {
    data object Loading: CommentState()
    data class Error(val error: String): CommentState()
    data class Success(val comments: MutableList<CommentResponse>): CommentState()
}