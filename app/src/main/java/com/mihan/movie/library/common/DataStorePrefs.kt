package com.mihan.movie.library.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStorePrefs @Inject constructor(@ApplicationContext context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)
    private val dataStore = context.dataStore
    fun getAppUpdates(): Flow<Boolean> =
        dataStore.data.map { prefs ->
            prefs[APP_UPDATES_KEY] ?: false
        }

    suspend fun setAppUpdates(isUpdateAvailable: Boolean) =
        dataStore.edit { prefs ->
            prefs[APP_UPDATES_KEY] = isUpdateAvailable
        }

    companion object {
        private const val DATA_STORE_NAME = "data_store_preferences"
        private val APP_UPDATES_KEY = booleanPreferencesKey("app_updates_key")
    }
}