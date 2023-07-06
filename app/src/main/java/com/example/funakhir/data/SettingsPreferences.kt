package com.example.funakhir.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsPreferences(private val context: Context) {
    private val Context.settingsPreferenceDataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATA_STORE_NAME
    )

    fun getThemeSetting(): Flow<Boolean> {
        return context.settingsPreferenceDataStore.data.map {
            it[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        context.settingsPreferenceDataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: SettingsPreferences? = null

        fun getInstance(context: Context): SettingsPreferences =
            INSTANCE?: synchronized(this) {
                val instance = INSTANCE?: SettingsPreferences(context).also { INSTANCE = it }
                instance
            }

        const val DATA_STORE_NAME = "SETTINGS_DATASTORE"
        private val THEME_KEY = booleanPreferencesKey("theme_setting")
    }
}