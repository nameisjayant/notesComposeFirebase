package com.nameisjayant.notecomposeapp.utils


import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceStore @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    companion object {
        private val Context.datastore by preferencesDataStore(PREFERENCE)
        val index = stringPreferencesKey("index")
        val userId = stringPreferencesKey("userId")
        val email = stringPreferencesKey("email")
    }

    suspend fun setPref(
        key: Preferences.Key<String>,
        value: String
    ) = context
        .datastore.edit { preference ->
            preference[key] = value
        }

    fun getPref(key: Preferences.Key<String>): Flow<String> = context
        .datastore.data
        .catch {
            if (this is Exception) {
                emit(emptyPreferences())
            }
        }.map { preference ->
            val data = preference[key] ?: "-"
            data
        }

    suspend fun clearDataStore() {
        context.datastore.edit {
            it.clear()
        }
    }
}