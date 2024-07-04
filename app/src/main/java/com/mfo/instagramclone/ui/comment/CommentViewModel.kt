package com.mfo.instagramclone.ui.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.instagramclone.data.network.response.CommentResponse
import com.mfo.instagramclone.domain.usecase.GetCommentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(private val getCommentsUseCase: GetCommentsUseCase): ViewModel() {
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
}