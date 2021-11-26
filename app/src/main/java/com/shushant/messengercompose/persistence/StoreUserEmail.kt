package com.shushant.messengercompose.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreUserEmail(private val context: Context) {

    // to make sure there's only one instance
    companion object {
        private val Context.dataStoree: DataStore<Preferences> by preferencesDataStore("messengerCompose")
        val USER_KEY = stringPreferencesKey("saveUser")
    }
    
    //get the saved email
    val getUser: Flow<String?> = context.dataStoree.data
        .map { preferences ->
            preferences[USER_KEY] ?: "FirstLast@gmail.com"
        }

    //save email into datastore
    suspend fun saveUser(name: String) {
        context.dataStoree.edit { preferences ->
            preferences[USER_KEY] = name
        }
    }


}