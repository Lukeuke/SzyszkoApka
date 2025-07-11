package com.szyszkodar.szyszkoapka.data.localDB

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

object AppPreferences {
    private val launchCountKey = intPreferencesKey(name = "launchCount")

     suspend fun getLaunchCount(context: Context): Int {
        return context.dataStore.data
            .map { it[launchCountKey] ?: 0 }
            .first()
    }

    suspend fun updateLaunchCount(context: Context) {
        context.dataStore.edit { prefs ->
            val current = prefs[launchCountKey] ?: 0

            prefs[launchCountKey] = current + 1
        }
    }
}