package com.mfo.instagramclone.ui.setting.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mfo.instagramclone.databinding.ItemSettingBinding
import com.mfo.instagramclone.domain.models.SettingInfo

class SettingViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemSettingBinding.bind(view)

    fun bind(settingInfo: SettingInfo, onItemSelected: (SettingInfo) -> Unit) {
        val context = binding.tvTitle.context
        binding.ivIcon.setImageResource(settingInfo.img)
        binding.tvTitle.text = context.getString(settingInfo.name)

        binding.parent.setOnClickListener { onItemSelected(settingInfo) }
    }
}