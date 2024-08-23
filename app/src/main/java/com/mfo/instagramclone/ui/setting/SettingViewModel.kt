package com.mfo.instagramclone.ui.setting

import androidx.lifecycle.ViewModel
import com.mfo.instagramclone.data.providers.SettingProvider
import com.mfo.instagramclone.domain.models.SettingInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(settingProvider: SettingProvider): ViewModel() {
    private var _setting = MutableStateFlow<List<SettingInfo>>(emptyList())
    val setting: StateFlow<List<SettingInfo>> = _setting

    init {
        _setting.value = settingProvider.getSettings()
    }
}