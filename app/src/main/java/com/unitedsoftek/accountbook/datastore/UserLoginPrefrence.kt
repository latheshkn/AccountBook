package com.unitedsoftek.accountbook.datastore


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserLoginPrefrence(val mDataStore: DataStore<Preferences>) {
    companion object {
        val USERNAME = stringPreferencesKey("username")
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD = stringPreferencesKey("password")
        val MOBILE = stringPreferencesKey("mobile")
        val SECONDARY_MOBILE = stringPreferencesKey("secondary_mobile")
        val IMAGE = stringPreferencesKey("image")
        val LOGO = stringPreferencesKey("logo")
        val BUSSINESS_NAME = stringPreferencesKey("business_name")
        val GST = stringPreferencesKey("gst")
        val BUSINESS_ADDRESS = stringPreferencesKey("business_address")
    }

    suspend fun saveUserDetails(
        mobile:String,
    ){
        mDataStore.edit {preferences ->

            preferences[MOBILE] = mobile

        }
    }

    suspend fun logout() {
        mDataStore.edit {
            it.clear()
        }
    }

    val mobileFlow: Flow<String?> = mDataStore.data.map { preferences ->
        preferences[MOBILE]

    }

    val emailFlow:Flow<String?> = mDataStore.data.map{
        it[EMAIL]
    }
}