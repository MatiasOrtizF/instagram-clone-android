package com.mfo.instagramclone.ui.follow.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfo.instagramclone.R
import com.mfo.instagramclone.data.network.response.FollowResponse
import com.mfo.instagramclone.databinding.ItemUserBinding
import com.mfo.instagramclone.utils.Constants

class FollowViewHolder (view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemUserBinding.bind(view)

    fun bind(
        user: FollowResponse,
        onItemSelected: (FollowResponse) -> Unit,
        onFollowToggleButtonClick: (Long, Boolean, Int) -> Unit,
        position: Int
    ) {
        val context = binding.ivProfile.context
        if(user.imageProfile != null) {
            Glide.with(context).load(user.imageProfile).circleCrop().into(binding.ivProfile)
        } else {
            Glide.with(context).load(Constants.IMG_PROFILE_DEFAULT).circleCrop().into(binding.ivProfile)
        }
        if(user.followed) {
            binding.btnDeleteFollow.isVisible = true
            binding.btnAddFollow.isVisible = false
        } else {
            binding.btnDeleteFollow.isVisible = false
            binding.btnAddFollow.isVisible = true
        }
        binding.tvUserName.text = user.userName
        binding.tvFullName.text = context.getString(R.string.full_name_format, user.name, user.lastName)

        //binding.parent.setOnClickListener { onItemSelected(user) }

        binding.btnAddFollow.setOnClickListener { onFollowToggleButtonClick(user.id, user.followed, position) }
        binding.btnDeleteFollow.setOnClickListener { onFollowToggleButtonClick(user.id, user.followed, position) }

        //binding.ivProfile.setOnClickListener { onItemSelected(post) }
    }
}