package com.mfo.instagramclone.ui.comment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfo.instagramclone.R
import com.mfo.instagramclone.data.network.response.CommentResponse

class CommentAdapter (private var commentList: MutableList<CommentResponse> = mutableListOf(), private val onItemSelected: (CommentResponse) -> Unit): RecyclerView.Adapter<CommentViewHolder>() {
    fun updateList(list: MutableList<CommentResponse>) {
        commentList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_comment_list_dialog_list_dialog_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(commentList[position], onItemSelected)
    }
}