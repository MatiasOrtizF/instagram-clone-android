package com.mfo.instagramclone.ui.postActions.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfo.instagramclone.data.network.response.PostActionResponse
import com.mfo.instagramclone.databinding.ItemProfileBinding

class PostActionViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemProfileBinding.bind(view)

    fun bind(post: PostActionResponse, onItemSelected: (PostActionResponse) -> Unit) {
        val context = binding.ivPost.context
        Glide.with(context).load(post.img).into(binding.ivPost)

        binding.ivPost.setOnClickListener { onItemSelected(post) }
    }
}