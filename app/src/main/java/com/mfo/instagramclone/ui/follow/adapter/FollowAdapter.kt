package com.mfo.instagramclone.ui.follow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfo.instagramclone.R
import com.mfo.instagramclone.data.network.response.FollowResponse

class FollowAdapter(private var userList: List<FollowResponse> = emptyList(), private val onItemSelected: (FollowResponse) -> Unit, private val onFollowToggleButtonClick: (FollowResponse) -> Unit): RecyclerView.Adapter<FollowViewHolder>() {

    fun updateList(list: List<FollowResponse>) {
        userList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        return FollowViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        holder.bind(userList[position], onItemSelected, onFollowToggleButtonClick)
    }

}