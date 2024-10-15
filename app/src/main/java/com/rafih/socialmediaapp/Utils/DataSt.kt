package com.rafih.socialmediaapp.Utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="user")

object PreferencesKeys {
    val JWT = stringPreferencesKey("jwt_token")
}

suspend fun saveLoginToken(context: Context,jwt_token:String){
    context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.JWT] = jwt_token
    }
}

fun getLoginToken(context: Context): Flow<String> {
    return context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.JWT] ?: ""
    }
}

suspend fun clearLoginInfo(context: Context){
    context.dataStore.edit { preferences ->
        preferences.clear()
    }
}