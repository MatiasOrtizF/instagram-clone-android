package com.mfo.instagramclone.ui.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.instagramclone.data.network.response.CommentResponse
import com.mfo.instagramclone.domain.usecase.GetCommentsUseCase
import com.mfo.instagramclone.domain.usecase.comment.PostCommentUseCase
import com.mfo.instagramclone.domain.usecase.like.AddCommentLikeUseCase
import com.mfo.instagramclone.domain.usecase.like.DeleteCommentLikeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val postCommentsUseCase: PostCommentUseCase,
    private val addCommentLikeUseCase: AddCommentLikeUseCase,
    private val deleteCommentLikeUseCase: DeleteCommentLikeUseCase
): ViewModel() {
    private var _comment = MutableStateFlow<List<CommentResponse>>(emptyList())
    val comment: StateFlow<List<CommentResponse>> = _comment

    private var _state = MutableStateFlow<CommentState>(CommentState.Loading)
    val state: StateFlow<CommentState> = _state

    fun getComments(token: String, postId: Long) {
        viewModelScope.launch {
            _state.value = CommentState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getCommentsUseCase(token, postId) }
                if(result != null) {
                    _comment.value = result
                    _state.value = CommentState.Success(result.toMutableList())
                } else {
                    _state.value = CommentState.Error("Error occurred, Please try again later.")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = CommentState.Error(errorMessage)
            }
        }
    }

    fun addComment(token: String, postId: Long, comment: String) {
        viewModelScope.launch {
            _state.value = CommentState.Loading
            try {
                val result = withContext(Dispatchers.IO) { postCommentsUseCase(token, postId, comment) }
                if(result != null) {
                    _state.value = CommentState.SendSuccess(result)
                } else {
                    _state.value = CommentState.Error("Error occurred, Please try again later.")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = CommentState.Error(errorMessage)
            }
        }
    }

    fun addCommentLike(token: String, commentId: Long, position: Int) {
        viewModelScope.launch {
            //_state.value = CommentState.Loading
            try {
                val result = withContext(Dispatchers.IO) { addCommentLikeUseCase(token, commentId) }
                if(result != null) {
                    _state.value = CommentState.LikeSuccess(result, position)
                } else {
                    _state.value = CommentState.Error("Error occurred, Please try again later.")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = CommentState.Error(errorMessage)
            }
        }
    }

    fun deleteCommentLike(token: String, commentId: Long, position: Int) {
        viewModelScope.launch {
            //_state.value = CommentState.Loading
            try {
                val result = withContext(Dispatchers.IO) { deleteCommentLikeUseCase(token, commentId) }
                if(result != null) {
                    _state.value = CommentState.LikeSuccess(result, position)
                } else {
                    _state.value = CommentState.Error("Error occurred, Please try again later.")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = CommentState.Error(errorMessage)
            }
        }
    }
}