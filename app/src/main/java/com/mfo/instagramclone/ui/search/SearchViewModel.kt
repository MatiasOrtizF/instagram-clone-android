package com.mfo.instagramclone.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.instagramclone.domain.usecase.GetUserSearchByUserNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val getUserSearchByUserNameUseCase: GetUserSearchByUserNameUseCase): ViewModel() {
    private var _state = MutableStateFlow<SearchState>(SearchState.Loading)
    val state: StateFlow<SearchState> = _state

    fun getUserByUserName(token: String, word: String) {
        viewModelScope.launch {
            _state.value = SearchState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getUserSearchByUserNameUseCase(token, word) }
                if(result != null) {
                    _state.value = SearchState.Success(result)
                } else {
                    _state.value = SearchState.Error("Error Occurred, please try again later. ")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = SearchState.Error(errorMessage)
            }
        }
    }
}