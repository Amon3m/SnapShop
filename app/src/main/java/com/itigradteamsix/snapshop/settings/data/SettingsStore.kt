package com.itigradteamsix.snapshop.settings.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.itigradteamsix.snapshop.MyApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settingsStore")





data class UserPreferences(
    val isFirstTime: Boolean, //local
    val isLoggedIn: Boolean, //local
    val isGuest: Boolean, //local
    val customerId: Long, //api
    val customerName: String, //api
    val customerEmail: String, //api
    val userCurrency: String,  // local
    val cartDraftOrderId: Long, //api
    val metaFieldId: Long, //api
)

data class CurrencyPreferences(
    val currencyCode: String,
    val currencySymbol: String,
    val countryCode: String,
    val rate: Double
)




class SettingsStore(private val context: Context) {



    private val TAG: String = "UserPreferencesRepo"

    private var dataStore: DataStore<Preferences> = context.dataStore

    private object PreferencesKeys {
        val IS_FIRST_TIME = booleanPreferencesKey("is_first_time") //if yes, open onboarding, if no, open home
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in") //if yes, open home, if no, open login
        val IS_GUEST = booleanPreferencesKey("is_guest")
        val CUSTOMER_ID = longPreferencesKey("customer_id")
        val CUSTOMER_NAME = stringPreferencesKey("customer_name")
        val CUSTOMER_EMAIL = stringPreferencesKey("customer_email")
        val USER_CURRENCY = stringPreferencesKey("user_currency")
        val CART_DRAFT_ORDER_ID = longPreferencesKey("cart_draft_order_id")
        val META_FIELD_ID = longPreferencesKey("meta_field_id")

        val CURRENCY_CODE = stringPreferencesKey("currency_code")
        val CURRENCY_SYMBOL = stringPreferencesKey("currency_symbol")
        val COUNTRY_CODE = stringPreferencesKey("country_code")
        val RATE = doublePreferencesKey("rate")

    }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())



    //method to update the whole UserPreferences object at once
    suspend fun updateUserPreferences(userPreferences: UserPreferences) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_TIME] = userPreferences.isFirstTime
            preferences[PreferencesKeys.IS_LOGGED_IN] = userPreferences.isLoggedIn
            preferences[PreferencesKeys.IS_GUEST] = userPreferences.isGuest
            preferences[PreferencesKeys.CUSTOMER_ID] = userPreferences.customerId
            preferences[PreferencesKeys.CUSTOMER_NAME] = userPreferences.customerName
            preferences[PreferencesKeys.CUSTOMER_EMAIL] = userPreferences.customerEmail
            preferences[PreferencesKeys.USER_CURRENCY] = userPreferences.userCurrency
            preferences[PreferencesKeys.CART_DRAFT_ORDER_ID] = userPreferences.cartDraftOrderId
            preferences[PreferencesKeys.META_FIELD_ID] = userPreferences.metaFieldId
        }
    }

    /**
     * Get the user preferences flow.
     */
    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }


    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        val isFirstTime = preferences[PreferencesKeys.IS_FIRST_TIME] ?: true
        val isLoggedIn = preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
        val isGuest = preferences[PreferencesKeys.IS_GUEST] ?: true
        val customerId = preferences[PreferencesKeys.CUSTOMER_ID] ?: 0
        val customerName = preferences[PreferencesKeys.CUSTOMER_NAME] ?: ""
        val customerEmail = preferences[PreferencesKeys.CUSTOMER_EMAIL] ?: ""
        val userCurrency = preferences[PreferencesKeys.USER_CURRENCY] ?: "usd"
        val cartDraftOrderId = preferences[PreferencesKeys.CART_DRAFT_ORDER_ID] ?: 0L
        val metaFieldId = preferences[PreferencesKeys.META_FIELD_ID] ?: 0L

        return UserPreferences(isFirstTime,isLoggedIn, isGuest, customerId, customerName, customerEmail, userCurrency,cartDraftOrderId,metaFieldId)
    }




    //method to update current draft order id
    suspend fun updateCartDraftOrderId(cartDraftOrderId: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CART_DRAFT_ORDER_ID] = cartDraftOrderId
        }
    }

    suspend fun updateMetaFieldId(metaFieldId: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.META_FIELD_ID] = metaFieldId
        }
    }


    val currencyPreferencesFlow: Flow<CurrencyPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapCurrencyPreferences(preferences)
        }

    private fun mapCurrencyPreferences(preferences: Preferences): CurrencyPreferences {
        val currencyCode = preferences[PreferencesKeys.CURRENCY_CODE] ?: "USD"
        val currencySymbol = preferences[PreferencesKeys.CURRENCY_SYMBOL] ?: "$"
        val countryCode = preferences[PreferencesKeys.COUNTRY_CODE] ?: "us"
        val rate = preferences[PreferencesKeys.RATE]  ?: 1.0

        return CurrencyPreferences(currencyCode, currencySymbol, countryCode, rate)
    }

    suspend fun updateCurrencyPreferences(currencyPreferences: CurrencyPreferences) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CURRENCY_CODE] = currencyPreferences.currencyCode
            preferences[PreferencesKeys.CURRENCY_SYMBOL] = currencyPreferences.currencySymbol
            preferences[PreferencesKeys.COUNTRY_CODE] = currencyPreferences.countryCode
            preferences[PreferencesKeys.RATE] = currencyPreferences.rate
        }
    }






}