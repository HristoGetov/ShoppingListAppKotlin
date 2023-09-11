package com.example.shoppinglistaappkotlin.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreManager(context: Context) {

    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "language")
    private val dataStore = context.dataStore

    companion object{
        val bulgarianLangKey = booleanPreferencesKey("BG_LANGUAGE_KEY")
    }

    suspend fun setLanguage(isBGLan : Boolean){
        dataStore.edit {pref ->
            pref[bulgarianLangKey] = isBGLan
        }
    }

    fun getLanguage() : Flow<Boolean> {
        return dataStore.data
            .catch {exception ->
                if (exception is IOException){
                    emit(emptyPreferences())
                }else{
                    throw exception
                }
            }
            .map {pref ->
                val uiMode = pref[bulgarianLangKey] ?:false
                uiMode
            }

    }
}