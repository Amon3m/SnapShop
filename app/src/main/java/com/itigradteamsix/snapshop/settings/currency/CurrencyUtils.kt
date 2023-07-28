package com.itigradteamsix.snapshop.settings.currency

import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.settings.data.CurrencyPreferences
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.ArrayList
import java.util.Currency
import java.util.Locale

@OptIn(DelicateCoroutinesApi::class)
object CurrencyUtils {

    //get currencyPreferences from settingsStore
//    lateinit var currencyPreferences: CurrencyPreferences
//    init {
//        //run blocking to get currencyPreferences
//        GlobalScope.launch {
//            MyApplication.appInstance.settingsStore.currencyPreferencesFlow.first().let {
//                currencyPreferences = it
//            }
//        }
//
//    }

    /**
     * A method for getting a country's currency symbol from the country's code
     * e.g USA - USD
     */

    fun getCurrencyCode(countryCode: String?): String? {
        val availableLocales = Locale.getAvailableLocales()
        for (i in availableLocales.indices) {
            if (availableLocales[i].country == countryCode
            ) return Currency.getInstance(availableLocales[i]).currencyCode
        }
        return ""
    }

    /*
    * To get a currency symbol (like $) from a currency code (like USD)
    * */
    fun getCurrencySymbol(currencyCode: String?): String {
        return try {
            Currency.getInstance(currencyCode).symbol
        } catch (e: Exception) {
            ""
        }
    }


    /**
     * A method for getting a country's code from the country name
     * e.g Nigeria - NG
     */

    fun getCountryCode(countryName: String) =
        Locale.getISOCountries().find { Locale("", it).displayCountry == countryName }


    /**
     * A method for getting all countries in the world - about 256 or so
     */

    fun getAllCountries(): ArrayList<String> {

        val locales = Locale.getAvailableLocales()
        val countries = ArrayList<String>()
        for (locale in locales) {
            val country = locale.displayCountry
            if (country.trim { it <= ' ' }.isNotEmpty() && !countries.contains(country)) {
                countries.add(country)
            }
        }
        countries.sort()
//        val index = countries.indexOf(getCountryName(currencyCode))

        return countries
    }


    //convert currency from one to another using the rate, amount is in dollars
    //give amount as double and currencyPref
    fun convertCurrency(originalPrice: Double?, currencyPreferences: CurrencyPreferences): String {
        return try {
            val convertedAmount = originalPrice?.times(currencyPreferences.rate)
            "${currencyPreferences.currencySymbol} ${String.format("%.2f", convertedAmount)}"
        } catch (e: Exception) {
            ""
        }
    }

    fun convertCurrencyWithoutSymbol(originalPriceString: String?, currencyPreferences: CurrencyPreferences): String {
        return try {
            val originalPrice = originalPriceString?.toDouble()
            val convertedAmount = originalPrice?.times(currencyPreferences.rate)
            String.format("%.2f", convertedAmount)
        } catch (e: Exception) {
            ""
        }
    }



}