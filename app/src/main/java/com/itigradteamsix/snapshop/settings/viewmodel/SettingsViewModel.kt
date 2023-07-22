package com.itigradteamsix.snapshop.settings.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.settings.data.SettingsStore
import com.itigradteamsix.snapshop.settings.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsStore: SettingsStore) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(
        getBetterDisplayName(
            AppCompatDelegate.getApplicationLocales().get(0)?.displayName
        )
    )
    val currentLanguage: MutableStateFlow<String> = _currentLanguage

    val userPreferencesFlow = settingsStore.userPreferencesFlow


    fun changeCurrency(currency: String) {
        viewModelScope.launch {
            userPreferencesFlow.collectLatest {
                val userPreferences = it
                val updatedUserPreferences = userPreferences.copy(userCurrency = currency)
                settingsStore.updateUserPreferences(updatedUserPreferences)
            }

        }

//        val userPreferences = userPreferencesFlow.value
//        val updatedUserPreferences = userPreferences.copy(currency = currency)
//        settingsStore.updateUserPreferences(updatedUserPreferences)
    }


    private fun getBetterDisplayName(language: String?): String {
        return if (language == "Arabic") {
            "العربية"
        } else {
            "English"
        }
    }

    fun changeLanguage(language: String) {
        if (language == "English") {
            val localeList = LocaleListCompat.forLanguageTags("en")
            AppCompatDelegate.setApplicationLocales(localeList)
        } else {
            val localeList = LocaleListCompat.forLanguageTags("ar")
            AppCompatDelegate.setApplicationLocales(localeList)
        }
        _currentLanguage.value =
            getBetterDisplayName(AppCompatDelegate.getApplicationLocales().get(0)?.displayName)

    }


}