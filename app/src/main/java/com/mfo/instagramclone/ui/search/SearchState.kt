package com.mfo.instagramclone.ui.search

import com.mfo.instagramclone.data.network.response.UserSearchResponse

sealed class SearchState {
    data object Loading: SearchState()
    data class Error(val error: String): SearchState()
    data class Success(val user: List<UserSearchResponse>): SearchState()
}