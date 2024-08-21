package com.mfo.instagramclone.ui.follow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.instagramclone.domain.usecase.follow.DeleteFollowerUseCase
import com.mfo.instagramclone.domain.usecase.follow.GetFollowersUseCase
import com.mfo.instagramclone.domain.usecase.follow.GetFollowingsUseCase
import com.mfo.instagramclone.domain.usecase.follow.PostFollowerUseCase
import com.mfo.instagramclone.domain.usecase.like.GetAllUsersLikedPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val getAllUsersLikedPostUseCase: GetAllUsersLikedPostUseCase,
    private val getFollowersUseCase: GetFollowersUseCase,
    private val getFollowingsUseCase: GetFollowingsUseCase,
    private val postFollowerUseCase: PostFollowerUseCase,
    private val deleteFollowerUseCase: DeleteFollowerUseCase
): ViewModel() {
    private var _state = MutableStateFlow<FollowState>(FollowState.Loading)
    val state: StateFlow<FollowState> = _state

    fun getUsersLikedPost(token: String, postId: Long) {
        viewModelScope.launch {
            _state.value = FollowState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getAllUsersLikedPostUseCase(token, postId) }
                if(result != null) {
                    _state.value = FollowState.Success(result)
                } else {
                    _state.value = FollowState.Error("Error Occurred, please try again later. ")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = FollowState.Error(errorMessage)
            }
        }
    }

    fun getFollowers(token: String, userId: Long) {
        viewModelScope.launch {
            _state.value = FollowState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getFollowersUseCase(token, userId) }
                if(result != null) {
                    _state.value = FollowState.Success(result)
                } else {
                    _state.value = FollowState.Error("Error Occurred, please try again later. ")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = FollowState.Error(errorMessage)
            }
        }
    }

    fun getFollowings(token: String, userId: Long) {
        viewModelScope.launch {
            _state.value = FollowState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getFollowingsUseCase(token, userId) }
                if(result != null) {
                    _state.value = FollowState.Success(result)
                } else {
                    _state.value = FollowState.Error("Error Occurred, please try again later. ")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = FollowState.Error(errorMessage)
            }
        }
    }

    fun addFollower(token: String, userId: Long) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { postFollowerUseCase(token, userId) }
                if(result != null) {
                    _state.value = FollowState.FollowSuccess(result)
                } else {
                    _state.value = FollowState.Error("Error Occurred, please try again later.")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = FollowState.Error(errorMessage)
            }
        }
    }

    fun deleteFollower(token: String, userId: Long) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { deleteFollowerUseCase(token, userId) }
                if(result != null) {
                    _state.value = FollowState.FollowSuccess(result)
                } else {
                    _state.value = FollowState.Error("Error Occurred, please try again later.")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = FollowState.Error(errorMessage)
            }
        }
    }
}