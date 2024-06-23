package com.mfo.instagramclone.ui.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfo.instagramclone.R
import com.mfo.instagramclone.data.network.response.UserResponse

class ProfileAdapter(private var postList: List<UserResponse.UserPost> = emptyList(), private val onItemSelected: (UserResponse.UserPost) -> Unit): RecyclerView.Adapter<ProfileViewHolder>() {
    fun updateList(list: List<UserResponse.UserPost>) {
        postList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        return ProfileViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(postList[position], onItemSelected)
    }

}