package com.mfo.instagramclone.ui.postActions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.instagramclone.domain.usecase.comment.GetAllCommentsUseCase
import com.mfo.instagramclone.domain.usecase.like.GetAllLikesUseCase
import com.mfo.instagramclone.domain.usecase.save.GetAllSaveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PostActionViewModel @Inject constructor(
    private val getAllSaveUseCase: GetAllSaveUseCase,
    private val getAllLikesUseCase: GetAllLikesUseCase,
    private val getAllCommentsUseCase: GetAllCommentsUseCase
): ViewModel() {

    private var _state = MutableStateFlow<PostActionState>(PostActionState.Loading)
    val state: StateFlow<PostActionState> = _state

    fun getAllSave(token: String) {
        viewModelScope.launch {
            _state.value = PostActionState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getAllSaveUseCase(token) }
                if(result != null) {
                    _state.value = PostActionState.Success(result)
                } else {
                    _state.value = PostActionState.Error("Error Occurred, please try again later.")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = PostActionState.Error(errorMessage)
            }
        }
    }

    fun getAllLikes(token: String) {
        viewModelScope.launch {
            _state.value = PostActionState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getAllLikesUseCase(token) }
                if(result != null) {
                    _state.value = PostActionState.Success(result)
                } else {
                    _state.value = PostActionState.Error("Error Occurred, please try again later.")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = PostActionState.Error(errorMessage)
            }
        }
    }

    fun getAllComments(token: String) {
        viewModelScope.launch {
            _state.value = PostActionState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getAllCommentsUseCase(token) }
                if(result != null) {
                    _state.value = PostActionState.Success(result)
                } else {
                    _state.value = PostActionState.Error("Error Occurred, please try again later.")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = PostActionState.Error(errorMessage)
            }
        }
    }
}