package com.mfo.instagramclone.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfo.instagramclone.R
import com.mfo.instagramclone.data.network.response.UserHistoryResponse
import com.mfo.instagramclone.data.network.response.UserSearchResponse

class SearchAdapter(private var userList: MutableList<UserHistoryResponse> = mutableListOf(), private val onItemSelected: (UserHistoryResponse) -> Unit, private val onHistoryDeleteButtonClicked: (Long, Int) -> Unit): RecyclerView.Adapter<SearchViewHolder>() {
    fun updateList(list: MutableList<UserHistoryResponse>) {
        userList = list
        notifyDataSetChanged()
    }

    fun onDeleteItem(position: Int) {
        userList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_search, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(userList[position], onItemSelected, onHistoryDeleteButtonClicked)
    }
}