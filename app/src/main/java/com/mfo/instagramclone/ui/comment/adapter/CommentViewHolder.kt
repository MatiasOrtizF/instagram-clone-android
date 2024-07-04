package com.mfo.instagramclone.ui.comment.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfo.instagramclone.data.network.response.CommentResponse
import com.mfo.instagramclone.databinding.FragmentCommentListDialogListDialogItemBinding

class CommentViewHolder (view: View): RecyclerView.ViewHolder(view) {
    private val binding = FragmentCommentListDialogListDialogItemBinding.bind(view)

    fun bind(comment: CommentResponse, onItemSelected: (CommentResponse) -> Unit) {
        val context = binding.ivProfile.context
        if(comment.user.image != null) {
            Glide.with(context).load(comment.user.image).circleCrop().into(binding.ivProfile)
        }
        if(comment.user.verified) {
            binding.ivVerified.isVisible = true
        }
        binding.tvUserName.text = comment.user.userName
        binding.tvComment.text = comment.content
        binding.tvLikes.text = comment.likes.toString()

        //binding.ivProfile.setOnClickListener { onItemSelected(post) }
    }
}