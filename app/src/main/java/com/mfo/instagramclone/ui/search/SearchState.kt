package com.mfo.instagramclone.ui.search

import com.mfo.instagramclone.data.network.response.UserHistoryResponse

sealed class SearchState {
    data object Loading: SearchState()
    data class Error(val error: String): SearchState()
    data class Success(val users: MutableList<UserHistoryResponse>): SearchState()
    data class HistorySuccess(val success: Map<String, Boolean>?): SearchState()
}