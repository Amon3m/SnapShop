package com.itigradteamsix.snapshop.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itigradteamsix.snapshop.settings.currency.MainRepo
import com.itigradteamsix.snapshop.settings.data.SettingsStore

class SettingsViewModelFactory(private val settingsStore: SettingsStore,private  val mainRepo: MainRepo
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(settingsStore,mainRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
