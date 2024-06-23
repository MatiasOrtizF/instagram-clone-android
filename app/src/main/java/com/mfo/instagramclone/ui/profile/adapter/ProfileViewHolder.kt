package com.mfo.instagramclone.ui.profile.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfo.instagramclone.data.network.response.UserResponse
import com.mfo.instagramclone.databinding.ItemProfileBinding

class ProfileViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemProfileBinding.bind(view)

    fun bind(post: UserResponse.UserPost, onItemSelected: (UserResponse.UserPost) -> Unit) {
        val context = binding.ivPost.context
        Glide.with(context).load(post.image).into(binding.ivPost)

        binding.ivPost.setOnClickListener { onItemSelected(post) }
    }
}