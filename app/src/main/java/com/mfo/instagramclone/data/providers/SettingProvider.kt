package com.mfo.instagramclone.data.providers

import com.mfo.instagramclone.domain.models.SettingInfo
import javax.inject.Inject

class SettingProvider @Inject constructor() {
    fun getSettings(): List<SettingInfo> {
        return listOf(
            SettingInfo.Saved,
            SettingInfo.Like,
            SettingInfo.Comment
        )
    }
}