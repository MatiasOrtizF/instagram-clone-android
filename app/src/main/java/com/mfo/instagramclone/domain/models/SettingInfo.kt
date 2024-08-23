package com.mfo.instagramclone.domain.models

import com.mfo.instagramclone.R

sealed class SettingInfo(val img: Int, val name: Int) {
    object Saved: SettingInfo(R.drawable.ic_save, R.string.tv_saved)
    object Like: SettingInfo(R.drawable.ic_like, R.string.tv_like)
    object Comment: SettingInfo(R.drawable.ic_comment, R.string.tv_comment)
}