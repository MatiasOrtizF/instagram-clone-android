package com.mfo.instagramclone.ui.splash

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
class SplashViewModel @Inject constructor(private val getUserInfoUseCase: GetUserInfoUseCase): ViewModel() {
    private var _state = MutableStateFlow<SplashState>(SplashState.Loading)
    val state: StateFlow<SplashState> = _state

    fun getUserInfo(token: String) {
        viewModelScope.launch {
            _state.value = SplashState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getUserInfoUseCase(token) }
                if(result != null) {
                    _state.value = SplashState.Success(result)
                } else {
                    _state.value = SplashState.Error("Error Occurred, please try again later. ")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = SplashState.Error(errorMessage)
            }
        }
    }
}