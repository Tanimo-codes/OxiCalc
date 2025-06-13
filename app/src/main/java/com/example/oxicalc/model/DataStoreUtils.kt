package com.example.oxicalc.model

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create a preference key
val Context.dataStore by preferencesDataStore(name = "settings")
val ONBOARDING_KEY = booleanPreferencesKey("onboarding_completed")

suspend fun saveOnboardingState(context: Context, completed: Boolean) {
    context.dataStore.edit { it[ONBOARDING_KEY] = completed }
}

fun readOnboardingState(context: Context): Flow<Boolean> =
    context.dataStore.data.map { it[ONBOARDING_KEY] ?: false }
