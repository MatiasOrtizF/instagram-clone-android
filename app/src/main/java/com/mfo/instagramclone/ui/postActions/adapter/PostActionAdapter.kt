package com.mfo.instagramclone.ui.postActions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfo.instagramclone.R
import com.mfo.instagramclone.data.network.response.PostActionResponse

class PostActionAdapter(private var postList: List<PostActionResponse> = emptyList(), private val onItemSelected: (PostActionResponse) -> Unit): RecyclerView.Adapter<PostActionViewHolder>() {
    fun updateList(list: List<PostActionResponse>) {
        postList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostActionViewHolder {
        return PostActionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostActionViewHolder, position: Int) {
        holder.bind(postList[position], onItemSelected)
    }
}