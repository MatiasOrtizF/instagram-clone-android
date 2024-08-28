package com.mfo.instagramclone.ui.comment

import com.mfo.instagramclone.data.network.response.CommentResponse

sealed class CommentState {
    data object Loading: CommentState()
    data class Error(val error: String): CommentState()
    data class Success(val comments: MutableList<CommentResponse>): CommentState()
    data class SendSuccess(val comment: CommentResponse): CommentState()
    data class LikeSuccess(val success: Map<String, Boolean>?, val position: Int): CommentState()
}