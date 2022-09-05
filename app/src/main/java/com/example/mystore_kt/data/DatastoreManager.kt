package com.example.mystore_kt.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mystore_kt.data.DatastoreManager.PreferencesKeys.USER_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton //You can ignore this annotation as return `datastore` from `preferencesDataStore` is singleton
class DatastoreManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {
    private val Context.dataStore by preferencesDataStore("user_account")
    private val userAccountDataStore = appContext.dataStore

    suspend fun setUserId(userId: Int) {
        userAccountDataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun removeUserId(){
        userAccountDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val userId: Flow<Int?> = userAccountDataStore.data.map { preferences ->
        preferences[USER_ID]
    }


    private object PreferencesKeys {
        val USER_ID = intPreferencesKey("user_id")
    }
}