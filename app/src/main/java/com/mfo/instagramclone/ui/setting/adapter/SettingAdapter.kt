package com.mfo.instagramclone.ui.setting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mfo.instagramclone.R
import com.mfo.instagramclone.domain.models.SettingInfo

class SettingAdapter(private var settingList: List<SettingInfo> = emptyList(), private val onItemSelected:(SettingInfo) -> Unit): RecyclerView.Adapter<SettingViewHolder>() {

    fun updateList(list: List<SettingInfo>) {
        settingList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingViewHolder {
        return SettingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_setting, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return settingList.size
    }

    override fun onBindViewHolder(holder: SettingViewHolder, position: Int) {
        holder.bind(settingList[position], onItemSelected)
    }
}