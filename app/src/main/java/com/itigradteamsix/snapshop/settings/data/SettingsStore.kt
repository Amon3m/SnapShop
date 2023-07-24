package com.itigradteamsix.snapshop.settings.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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

val KEY_USER_TYPE = stringPreferencesKey("user_type_key") //guest or user

enum class UserType(var value: String){
    GUEST("guest"),
    USER("user")
}


data class UserPreferences(
    val isFirstTime: Boolean, //local
    val isLoggedIn: Boolean, //local
    val isGuest: Boolean, //local
    val customerId: Long, //api
    val customerName: String, //api
    val customerEmail: String, //api
    val userCurrency: String,  // local
    val cartDraftOrderId: Long //api
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
        val cartDraftOrderId = preferences[PreferencesKeys.CART_DRAFT_ORDER_ID] ?: 0

        return UserPreferences(isFirstTime,isLoggedIn, isGuest, customerId, customerName, customerEmail, userCurrency,cartDraftOrderId)
    }


    suspend fun updateUserCurrency(userCurrency: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_CURRENCY] = userCurrency
        }
    }

    //update user id, name, email at once  ( also set isGuest to false)
    suspend fun updateCustomerInfo(customerId: Long, customerName: String, customerEmail: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_GUEST] = false
            preferences[PreferencesKeys.CUSTOMER_ID] = customerId
            preferences[PreferencesKeys.CUSTOMER_NAME] = customerName
            preferences[PreferencesKeys.CUSTOMER_EMAIL] = customerEmail
        }
    }

    //method to update current draft order id
    suspend fun updateCartDraftOrderId(cartDraftOrderId: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CART_DRAFT_ORDER_ID] = cartDraftOrderId
        }
    }




    val userType: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[KEY_USER_TYPE] ?: UserType.GUEST.value
        }

    suspend fun setUserType(userType: UserType) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_TYPE] = userType.value
        }
    }






}