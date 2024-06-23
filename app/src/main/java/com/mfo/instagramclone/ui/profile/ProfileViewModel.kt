package com.mfo.instagramclone.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.instagramclone.domain.usecase.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val getUserInfoUseCase: GetUserInfoUseCase): ViewModel() {
    private var _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state: StateFlow<ProfileState> = _state

    fun getUserInfo(token: String) {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getUserInfoUseCase(token) }
                if(result != null) {
                    _state.value = ProfileState.Success(result)
                } else {
                    _state.value = ProfileState.Error("Error Occurred, please try again later. ")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = ProfileState.Error(errorMessage)
            }
        }
    }
}