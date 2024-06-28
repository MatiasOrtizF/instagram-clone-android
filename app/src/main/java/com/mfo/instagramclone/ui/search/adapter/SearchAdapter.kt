package com.mfo.instagramclone.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfo.instagramclone.R
import com.mfo.instagramclone.data.network.response.UserSearchResponse

class SearchAdapter(private var userList: List<UserSearchResponse> = emptyList(), private val onItemSelected: (UserSearchResponse) -> Unit): RecyclerView.Adapter<SearchViewHolder>()  {
    fun updateList(list: List<UserSearchResponse>) {
        userList = list
        notifyDataSetChanged()
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
        holder.bind(userList[position], onItemSelected)
    }
}