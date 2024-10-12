package com.rafih.socialmediaapp.Utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="user")

object PreferencesKeys {
    val ID = intPreferencesKey("id")
}

suspend fun saveLoginInfo(context: Context,id:Int){
    context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.ID] = id
    }
}

fun getLoginInfo(context: Context): Flow<Int> {
    return context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ID] ?: -1
    }
}

suspend fun clearLoginInfo(context: Context){
    context.dataStore.edit { preferences ->
        preferences.clear()
    }
}