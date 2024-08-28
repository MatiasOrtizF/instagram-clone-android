package com.mfo.instagramclone.ui.comment.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfo.instagramclone.R
import com.mfo.instagramclone.data.network.response.CommentResponse
import com.mfo.instagramclone.databinding.FragmentCommentListDialogListDialogItemBinding

class CommentViewHolder (view: View): RecyclerView.ViewHolder(view) {
    private val binding = FragmentCommentListDialogListDialogItemBinding.bind(view)

    fun bind(
        comment: CommentResponse,
        onItemSelected: (CommentResponse) -> Unit,
        onItemLiked: (Long, Int, Boolean) -> Unit,
        position: Int
    ) {
        val context = binding.ivProfile.context
        if(comment.user.imageProfile != null) {
            Glide.with(context).load(comment.user.imageProfile).circleCrop().into(binding.ivProfile)
        } else {
            Glide.with(context).load("https://t4.ftcdn.net/jpg/00/64/67/63/240_F_64676383_LdbmhiNM6Ypzb3FM4PPuFP9rHe7ri8Ju.jpg").circleCrop().into(binding.ivProfile)
        }
        if(comment.liked) {
            binding.btnLike.setImageResource(R.drawable.ic_liked)
        } else {
            binding.btnLike.setImageResource(R.drawable.ic_like)
        }
        binding.ivVerified.isVisible = comment.user.verified
        binding.tvUserName.text = comment.user.userName
        binding.tvComment.text = comment.content
        binding.tvLikes.text = comment.likes.toString()

        //binding.ivProfile.setOnClickListener { onItemSelected(post) }
        binding.btnLike.setOnClickListener { onItemLiked(comment.id, position, comment.liked) }
    }
}