package com.nutricionbn.insulator.core.data.repositories.calculation

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.nutricionbn.insulator.core.data.dto.user.DoseUploadRequest
import com.nutricionbn.insulator.core.data.repositories.user.SessionRepository
import com.nutricionbn.insulator.core.services.user.UserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.ConcurrentHashMap

data class DoseCalculationSnapshot(
    val isLoaded: Boolean = true,
    val bloodGlucose: Float = 0f,
    val carbs: Float = 0f,
    val ratio: Float = 0f,
    val sensitivity: Float = 0f,
    val targetGlucose: Float = 0f,
    val doseCorrection: Float = 0f,
    val foodDose: Float = 0f,
    val dose: Float = 0f,
    val calculatedAt: String = ""
)

// TODO : interface
class CalculationSnapshotRepository(
    private val context: Context,
    private val _sessionRepository: SessionRepository,
    private val _userService: UserService
) {

    private val _dataStoreCache =
        ConcurrentHashMap<String, DataStore<Preferences>>()

    private suspend fun getDataStore(): DataStore<Preferences> {
        val userId = _sessionRepository.userState.value.id

        return _dataStoreCache.getOrPut(userId) {
            PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("calc_snapshot_$userId") }
            )
        }
    }

    private companion object {
        val IS_LOADED = booleanPreferencesKey("is_loaded")
        val BLOOD_GLUCOSE = floatPreferencesKey("blood_glucose")
        val CARBS = floatPreferencesKey("carbs")
        val RATIO = floatPreferencesKey("ratio")
        val SENSITIVITY = floatPreferencesKey("sensitivity")
        val TARGET_GLUCOSE = floatPreferencesKey("target_glucose")
        val DOSE_CORRECTION = floatPreferencesKey("dose_correction")
        val FOOD_DOSE = floatPreferencesKey("food_dose")
        val DOSE = floatPreferencesKey("dose")
        val CALCULATED_AT = stringPreferencesKey("calculated_at")
    }

    suspend fun saveDoseCalculationSnapshot(snapshot: DoseCalculationSnapshot) {
        getDataStore().edit { preferences ->
            preferences[IS_LOADED] = snapshot.isLoaded
            preferences[BLOOD_GLUCOSE] = snapshot.bloodGlucose
            preferences[CARBS] = snapshot.carbs
            preferences[RATIO] = snapshot.ratio
            preferences[SENSITIVITY] = snapshot.sensitivity
            preferences[TARGET_GLUCOSE] = snapshot.targetGlucose
            preferences[DOSE_CORRECTION] = snapshot.doseCorrection
            preferences[FOOD_DOSE] = snapshot.foodDose
            preferences[DOSE] = snapshot.dose
            preferences[CALCULATED_AT] = snapshot.calculatedAt
        }
    }

    val lastDoseCalculationSnapshotFlow: Flow<DoseCalculationSnapshot> =
        flow {
            val store = getDataStore()
            emitAll(
                store.data.map { preferences ->
                    DoseCalculationSnapshot(
                        isLoaded = preferences[IS_LOADED] ?: true,
                        bloodGlucose = preferences[BLOOD_GLUCOSE] ?: 0f,
                        carbs = preferences[CARBS] ?: 0f,
                        ratio = preferences[RATIO] ?: 0f,
                        sensitivity = preferences[SENSITIVITY] ?: 0f,
                        targetGlucose = preferences[TARGET_GLUCOSE] ?: 0f,
                        doseCorrection = preferences[DOSE_CORRECTION] ?: 0f,
                        foodDose = preferences[FOOD_DOSE] ?: 0f,
                        dose = preferences[DOSE] ?: 0f,
                        calculatedAt = preferences[CALCULATED_AT] ?: ""
                    )
                }
            )
        }

    suspend fun uploadDoseCalculation(observations: String?): Result<Any> {
        val snapshot = getCalculationSnapshot()

        val request = DoseUploadRequest(
            bloodGlucose = snapshot.bloodGlucose,
            carbs = snapshot.carbs,
            ratio = snapshot.ratio,
            sensitivity = snapshot.sensitivity,
            targetGlucose = snapshot.targetGlucose,
            doseCorrection = snapshot.doseCorrection,
            foodDose = snapshot.foodDose,
            dose = snapshot.dose,
            observations = observations,
            createdAt = snapshot.calculatedAt
        )

        val result = _userService.uploadDose(request)

        if (result.isSuccess) {
            saveDoseCalculationSnapshot(snapshot.copy(isLoaded = true))
        }

        return result
    }

    suspend fun getCalculationSnapshot(): DoseCalculationSnapshot {
        return lastDoseCalculationSnapshotFlow.first()
    }

    suspend fun discardSnapshot() {
        saveDoseCalculationSnapshot(DoseCalculationSnapshot())
    }
}



