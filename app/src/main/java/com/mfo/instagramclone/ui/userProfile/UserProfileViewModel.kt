package com.mfo.instagramclone.ui.userProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.instagramclone.domain.usecase.GetUserUseCase
import com.mfo.instagramclone.ui.profile.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val getUserUseCase: GetUserUseCase): ViewModel() {
    private var _state = MutableStateFlow<UserProfileState>(UserProfileState.Loading)
    val state: StateFlow<UserProfileState> = _state

    fun getUser(token: String, userId: Long) {
        viewModelScope.launch {
            _state.value = UserProfileState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getUserUseCase(token, userId) }
                if(result != null) {
                    _state.value = UserProfileState.Success(result)
                } else {
                    _state.value = UserProfileState.Error("Error Occurred, please try again later. ")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = UserProfileState.Error(errorMessage)
            }
        }
    }
}