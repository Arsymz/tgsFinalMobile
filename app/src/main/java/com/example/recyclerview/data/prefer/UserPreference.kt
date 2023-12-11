package com.example.recyclerview.data.prefer

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveSession(userModel: UserModel) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = userModel.email
            preferences[PASS_KEY] = userModel.password
            preferences[LOGIN_KEY] = true
        }
    }

    suspend fun logout() = dataStore.edit { preferences -> preferences.clear() }


    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[EMAIL_KEY] ?: "",
                preferences[PASS_KEY] ?: "",
                preferences[LOGIN_KEY] ?: false
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASS_KEY = stringPreferencesKey("pass")
        private val LOGIN_KEY = booleanPreferencesKey("islogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this){
                val instance =UserPreference(dataStore)
                INSTANCE=instance
                instance
            }
        }
    }
}