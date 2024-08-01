package com.mfo.instagramclone.ui.postDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.instagramclone.domain.usecase.GetPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase
): ViewModel(){

    private var _state = MutableStateFlow<PostDetailState>(PostDetailState.Loading)
    val state: StateFlow<PostDetailState> = _state

    fun getPost(token: String, postId: Long) {
        viewModelScope.launch {
            _state.value = PostDetailState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getPostUseCase(token, postId) }
                if(result != null) {
                    _state.value = PostDetailState.Success(result)
                } else {
                    _state.value = PostDetailState.Error("Error Occurred, please try again later. ")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = PostDetailState.Error(errorMessage)
            }
        }
    }
}