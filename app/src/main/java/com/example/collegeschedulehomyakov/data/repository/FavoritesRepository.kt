package com.example.collegeschedulehomyakov.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favorites_prefs")

class FavoritesRepository(private val context: Context) {
    private val favoritesKey = stringSetPreferencesKey("favorite_groups")

    suspend fun addFavorite(groupName: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[favoritesKey] ?: emptySet()
            preferences[favoritesKey] = current + groupName
        }
    }

    suspend fun removeFavorite(groupName: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[favoritesKey] ?: emptySet()
            preferences[favoritesKey] = current - groupName
        }
    }

    val favorites: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[favoritesKey] ?: emptySet()
        }

    fun isFavorite(groupName: String): Flow<Boolean> {
        return favorites.map { it.contains(groupName) }
    }
}