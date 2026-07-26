package com.nutricionbn.insulator.core.data.repositories.calculation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.nutricionbn.insulator.core.data.repositories.user.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.ConcurrentHashMap


data class UserCalculatorPrefs(
    val ratio: String,
    val sensitivity: String,
    val targetGlucose: String,
    val carbs: String
)

// TODO : interface
class CalculatorPreferencesRepository(
    private val context: Context,
    private val _sessionRepository: SessionRepository
) {

    private val _dataStoreCache =
        ConcurrentHashMap<String, DataStore<Preferences>>()

    private suspend fun getDataStore(): DataStore<Preferences> {
        val userId = _sessionRepository.userState.value.id

        return _dataStoreCache.getOrPut(userId) {
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("calc_prefs_$userId") }
            )
        }
    }

    private companion object {
        val RATIO = stringPreferencesKey("ratio")
        val SENSITIVITY = stringPreferencesKey("sensitivity")
        val TARGET_GLUCOSE = stringPreferencesKey("target_glucose")
        val CARBS = stringPreferencesKey("carbs")
    }

    val userPrefsFlow: Flow<UserCalculatorPrefs> =
        flow {
            val store = getDataStore()
            emitAll(
                store.data.map { preferences ->
                    UserCalculatorPrefs(
                        ratio = preferences[RATIO] ?: "",
                        sensitivity = preferences[SENSITIVITY] ?: "",
                        targetGlucose = preferences[TARGET_GLUCOSE] ?: "",
                        carbs = preferences[CARBS] ?: ""
                    )
                })
        }

    suspend fun saveRatio(ratio: String) {
        getDataStore().edit { preferences -> preferences[RATIO] = ratio }
    }

    suspend fun saveSensitivity(sensitivity: String) {
        getDataStore().edit { preferences -> preferences[SENSITIVITY] = sensitivity }
    }

    suspend fun saveTargetGlucose(targetGlucose: String) {
        getDataStore().edit { preferences ->
            preferences[TARGET_GLUCOSE] = targetGlucose
        }
    }

    suspend fun saveCarbs(carbs: String) {
        getDataStore().edit { preferences ->
            preferences[CARBS] = carbs
        }
    }

}
