package com.mfo.instagramclone.ui.follow.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfo.instagramclone.data.network.response.FollowResponse
import com.mfo.instagramclone.databinding.ItemUserBinding

class FollowViewHolder (view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemUserBinding.bind(view)

    fun bind(user: FollowResponse, onItemSelected: (FollowResponse) -> Unit, onFollowToggleButtonClick: (FollowResponse) -> Unit) {
        val context = binding.ivProfile.context
        if(user.imageProfile != null) {
            Glide.with(context).load(user.imageProfile).circleCrop().into(binding.ivProfile)
        }
        if(user.followed) {
            binding.btnDeleteFollow.isVisible = true
            binding.btnAddFollow.isVisible = false
        } else {
            binding.btnDeleteFollow.isVisible = false
            binding.btnAddFollow.isVisible = true
        }
        binding.tvUserName.text = user.userName
        binding.tvFullName.text = user.name + " " + user.lastName

        //binding.parent.setOnClickListener { onItemSelected(user) }

        binding.btnAddFollow.setOnClickListener { onFollowToggleButtonClick(user) }
        binding.btnDeleteFollow.setOnClickListener { onFollowToggleButtonClick(user) }

        //binding.ivProfile.setOnClickListener { onItemSelected(post) }
    }
}