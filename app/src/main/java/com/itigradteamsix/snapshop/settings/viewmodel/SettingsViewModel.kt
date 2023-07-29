package com.itigradteamsix.snapshop.settings.viewmodel

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itigradteamsix.snapshop.settings.currency.CurrencyUtils
import com.itigradteamsix.snapshop.settings.currency.CurrencyUtils.getCurrencySymbol
import com.itigradteamsix.snapshop.settings.currency.MainRepo
import com.itigradteamsix.snapshop.settings.currency.Resource
import com.itigradteamsix.snapshop.settings.data.CurrencyPreferences
import com.itigradteamsix.snapshop.settings.data.SettingsStore
import com.itigradteamsix.snapshop.settings.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsStore: SettingsStore, private val mainRepo: MainRepo) :
    ViewModel() {

    companion object {
        private const val TAG = "SettingsViewModel"
    }

    private val _currentLanguage = MutableStateFlow(
        getBetterDisplayName(
            AppCompatDelegate.getApplicationLocales().get(0)?.displayName
        )
    )
    val currentLanguage: MutableStateFlow<String> = _currentLanguage

    val userPreferencesFlow = settingsStore.userPreferencesFlow

    val currencyPreferencesFlow = settingsStore.currencyPreferencesFlow






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







    //Public function to get the result of conversion
    fun getConvertedData(
        access_key: String,
        from: String,
        to: String,
        amount: Double,
        countryCode: String
    ) {
        viewModelScope.launch {
            mainRepo.getConvertedData(access_key, from, to, amount).collect {
                val data = it.data
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        data?.let { it1 ->
                            Log.d(TAG, "getConvertedData on settings view model: $it1")
                            val rates = it1.rates
                            val convertionRate =
                                rates.values.toList()[0].rate.toDoubleOrNull() ?: 1.0
                            settingsStore.updateCurrencyPreferences(
                                CurrencyPreferences(
                                    to,
                                    getCurrencySymbol(to),
                                    countryCode,
                                    convertionRate
                                )
                            )
                        }

                    }

                    Resource.Status.ERROR -> {
//                        it.message?.let { it1 -> settingsStore.updateUserPreferences(it1) }
                    }

                    Resource.Status.LOADING -> {
//                        it.message?.let { it1 -> settingsStore.updateUserPreferences(it1) }
                    }
                }
            }
        }
    }


}