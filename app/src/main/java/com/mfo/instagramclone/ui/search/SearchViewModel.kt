package com.mfo.instagramclone.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfo.instagramclone.domain.usecase.search.DeleteUserSearchedInHistoryUseCase
import com.mfo.instagramclone.domain.usecase.search.GetUserSearchByUserNameUseCase
import com.mfo.instagramclone.domain.usecase.search.GetUsersSearchedHistoryUseCase
import com.mfo.instagramclone.domain.usecase.search.PostUserSearchedInHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getUsersSearchedHistoryUseCase: GetUsersSearchedHistoryUseCase,
    private val getUserSearchByUserNameUseCase: GetUserSearchByUserNameUseCase,
    private val postUserSearchedInHistoryUseCase: PostUserSearchedInHistoryUseCase,
    private val deleteUserSearchedInHistoryUseCase: DeleteUserSearchedInHistoryUseCase
): ViewModel() {

    private var _state = MutableStateFlow<SearchState>(SearchState.Loading)
    val state: StateFlow<SearchState> = _state

    fun getUsersSearchedHistory(token: String) {
        viewModelScope.launch {
            _state.value = SearchState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getUsersSearchedHistoryUseCase(token) }
                if(result != null) {
                    _state.value = SearchState.Success(result.toMutableList())
                } else {
                    _state.value = SearchState.Error("Error Occurred, please try again later. ")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = SearchState.Error(errorMessage)
            }
        }
    }

    fun getUserSearchByUserName(token: String, word: String) {
        viewModelScope.launch {
            _state.value = SearchState.Loading
            try {
                val result = withContext(Dispatchers.IO) { getUserSearchByUserNameUseCase(token, word) }
                if(result != null) {
                    _state.value = SearchState.Success(result.toMutableList())
                } else {
                    _state.value = SearchState.Error("Error Occurred, please try again later. ")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = SearchState.Error(errorMessage)
            }
        }
    }

    fun addUserSearchedInHistory(token: String, userId: Long) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { postUserSearchedInHistoryUseCase(token, userId) }
                if(result != null) {
                    _state.value = SearchState.HistorySuccess(result)
                } else {
                    _state.value = SearchState.Error("Error Occurred, please try again later.")
                }
            } catch (e: Exception) {
                val errorMessage: String = e.message.toString()
                _state.value = SearchState.Error(errorMessage)
            }
        }
    }

    suspend fun deleteUserSearchedInHistory(token: String, id: Long): Map<String, Boolean> {
        _state.value = SearchState.Loading
        return try {
            val result = withContext(Dispatchers.IO) { deleteUserSearchedInHistoryUseCase(token, id) }
            if (result!=null) {
                _state.value = SearchState.HistorySuccess(result)
                result
            } else {
                _state.value = SearchState.Error("Error occurred, please try again later.")
                mapOf("deleted" to false)
            }
        } catch (e: Exception) {
            val errorMessage: String = e.message.toString()
            _state.value = SearchState.Error(errorMessage)
            mapOf("deleted" to false)
        }
    }
}