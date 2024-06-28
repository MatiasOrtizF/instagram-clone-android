package com.mfo.instagramclone.ui.search.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfo.instagramclone.data.network.response.UserSearchResponse
import com.mfo.instagramclone.databinding.ItemUserSearchBinding

class SearchViewHolder (view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemUserSearchBinding.bind(view)

    fun bind(user: UserSearchResponse, onItemSelected: (UserSearchResponse) -> Unit) {
        val context = binding.ivProfile.context
        if(user.imageProfile != null) {
            Glide.with(context).load(user.imageProfile).circleCrop().into(binding.ivProfile)
        }
        binding.tvUserName.text = user.userName
        binding.tvFullName.text = user.name + " " + user.lastName

        //binding.ivProfile.setOnClickListener { onItemSelected(post) }
    }
}