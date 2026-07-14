package com.nutricionbn.insulator.core.data.repositories.calculation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "calculator_prefs")

data class UserCalculatorPrefs(
    val ratio: String,
    val sensitivity: String,
    val targetGlucose: String
)

// TODO : interface
class CalculatorPreferencesRepository(val dataStore: DataStore<Preferences>) {

    private companion object {
        val RATIO = stringPreferencesKey("ratio")
        val SENSITIVITY = stringPreferencesKey("sensitivity")
        val TARGET_GLUCOSE = stringPreferencesKey("target_glucose")
    }

    val userPrefsFlow: Flow<UserCalculatorPrefs> = dataStore.data.map { preferences ->
        UserCalculatorPrefs(
            ratio = preferences[RATIO] ?: "",
            sensitivity = preferences[SENSITIVITY] ?: "",
            targetGlucose = preferences[TARGET_GLUCOSE] ?: ""
        )
    }

    suspend fun saveRatio(ratio: String) {
        dataStore.edit { preferences -> preferences[RATIO] = ratio }
    }

    suspend fun saveSensitivity(sensitivity: String) {
        dataStore.edit { preferences -> preferences[SENSITIVITY] = sensitivity }
    }

    suspend fun saveTargetGlucose(targetGlucose: String) {
        dataStore.edit { preferences -> preferences[TARGET_GLUCOSE] = targetGlucose }
    }
}