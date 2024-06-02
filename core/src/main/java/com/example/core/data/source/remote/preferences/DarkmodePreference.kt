package com.example.core.data.source.remote.preferences

import android.content.Context
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.prefDataStore by preferencesDataStore(name = "settings")

class DarkmodePreferences(context: Context) {

    private val settingsDataStore = context.prefDataStore
    private val themeKey = booleanPreferencesKey("theme")

    suspend fun getTheme(): Boolean {
        return try {
            settingsDataStore.data.first()[themeKey] ?: false
        } catch (e: Exception) {
            // Handle exceptions
            false
        }
    }

    suspend fun setTheme(isDarkMode: Boolean) {
        try {
            settingsDataStore.edit { preferences ->
                preferences[themeKey] = isDarkMode
            }
        } catch (e: Exception) {
            // Handle exceptions
        }
    }
}

