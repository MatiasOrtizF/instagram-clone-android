package com.mfo.instagramclone.ui.follow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfo.instagramclone.R
import com.mfo.instagramclone.data.network.response.FollowResponse

class FollowAdapter(
    private var originalUserList: List<FollowResponse> = emptyList(),
    private var userList: List<FollowResponse> = emptyList(),
    private val onItemSelected: (FollowResponse) -> Unit,
    private val onFollowToggleButtonClick: (Long, Boolean, Int) -> Unit
): RecyclerView.Adapter<FollowViewHolder>() {

    fun updateList(list: List<FollowResponse>) {
        originalUserList = list
        userList = list
        notifyDataSetChanged()
    }

    fun filterList(userFilter: String) {
        userList = if(userFilter.isEmpty()) {
            originalUserList
        } else {
            originalUserList.filter { userName ->
                userName.userName.contains(userFilter, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    fun updateFollowState(position: Int, isFollowed: Boolean) {
        val user = userList[position]
        user.followed = isFollowed

        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        return FollowViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        holder.bind(userList[position], onItemSelected, onFollowToggleButtonClick, position)
    }

}