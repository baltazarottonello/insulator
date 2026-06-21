package com.example.insulator.ui.viewModels.calculation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insulator.core.data.dto.patient.ParametersResponse
import com.example.insulator.core.data.dto.patient.PatientParameters
import com.example.insulator.core.data.dto.patient.PatientResponse
import com.example.insulator.core.data.repositories.calculation.CalculatorPreferencesRepository
import com.example.insulator.core.data.repositories.patients.PatientRepository
import com.example.insulator.core.data.repositories.user.UserRepository
import com.example.insulator.core.data.source.FoodDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.round

class InsulatorCalculationViewModel(
    private val foodDataSource: FoodDataSource,
    private val preferencesRepository: CalculatorPreferencesRepository,
    private val userRepository: UserRepository,
    private val patientRepository: PatientRepository
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(CalculatorParamsUiState(foods = foodDataSource.getAvailableFoods()))
    val uiState: StateFlow<CalculatorParamsUiState> = _uiState.asStateFlow()

    val availableFoods = foodDataSource.getAvailableFoods()

    init {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            userRepository.userState.collect { user ->
                if (user.isLogged && user.isPatient) {
                    val response: ParametersResponse = patientRepository.getParameters()
                    _uiState.update {
                        it.copy(
                            ratio = response.params.ratio.toString(),
                            sensitivity = response.params.sens.toString(),
                            targetGlucose = response.params.targetGlucose.toString(),
                            isPatient = true,
                            isLoading = false,
                        )
                    }
                } else {
                    preferencesRepository.userPrefsFlow.collect { prefs ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                ratio = prefs.ratio,
                                sensitivity = prefs.sensitivity,
                                targetGlucose = prefs.targetGlucose,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun onGlucoseChanged(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(
                glucose = newValue,
            )
        }
    }

    fun onCarbsChanged(newValue: String) {
        _uiState.update { currentState ->
            currentState.copy(
                carbs = newValue,
            )
        }
    }

    fun cleanAutoCalculatedParams() {
        onCarbsChanged("")
    }

    fun addCarbsFromFood(food: Food) {
        val currentCarbs = _uiState.value.carbs.toIntOrNull() ?: 0
        val newCarbs = currentCarbs + food.carbsValue
        onCarbsChanged(newCarbs.toString())
    }

    fun onRatioChanged(newValue: String) {
        _uiState.update { it.copy(ratio = newValue) }
        viewModelScope.launch {
            preferencesRepository.saveRatio(newValue)
        }
    }

    fun onSensitivityChanged(newValue: String) {
        _uiState.update { it.copy(sensitivity = newValue) }
        viewModelScope.launch {
            preferencesRepository.saveSensitivity(newValue)
        }
    }

    fun onTargetGlucoseChanged(newValue: String) {
        _uiState.update { it.copy(targetGlucose = newValue) }
        viewModelScope.launch {
            preferencesRepository.saveTargetGlucose(newValue)
        }
    }

    fun onCalculateClicked() {
        val isGlucoseEmpty = _uiState.value.glucose.isEmpty()
        val isCarbsEmpty = _uiState.value.carbs.isEmpty()
        val isRatioEmpty = _uiState.value.ratio.isEmpty()
        val isSensEmpty = _uiState.value.sensitivity.isEmpty()
        val isTargetGlucoseEmpty = _uiState.value.targetGlucose.isEmpty()

        if (isGlucoseEmpty || isCarbsEmpty || isRatioEmpty || isSensEmpty || isTargetGlucoseEmpty) {
            _uiState.update {
                it.copy(
                    formValidationError = true,
                    showGlucoseError = isGlucoseEmpty,
                    showCarbsError = isCarbsEmpty,
                    showRatioError = isRatioEmpty,
                    showSensError = isSensEmpty,
                    showTargetGlucoseError = isTargetGlucoseEmpty
                )
            }
            return
        }

        calculateDosis()
    }

    fun calculateDosis() {
        val finalGlucose = _uiState.value.glucose.toFloatOrNull() ?: 0f
        val finalCarbs = _uiState.value.carbs.toFloatOrNull() ?: 0f
        val ratio = _uiState.value.ratio.toFloatOrNull() ?: 0f
        val sens = _uiState.value.sensitivity.toFloatOrNull() ?: 0f
        val targetGlucose = _uiState.value.targetGlucose.toFloatOrNull() ?: 0f

        val foodDosis = if (ratio > 0f) finalCarbs / ratio else 0f

        val dosisCorrection = if (sens > 0f && finalGlucose > targetGlucose) {
            (finalGlucose - targetGlucose) / sens
        } else {
            0f
        }

        var finalDosis = round((foodDosis + dosisCorrection) * 2) / 2
        if (finalDosis < 0f) finalDosis = 0f

        _uiState.update { currentState ->
            currentState.copy(
                formValidationError = false,
                showGlucoseError = false,
                showCarbsError = false,
                showRatioError = false,
                showSensError = false,
                showTargetGlucoseError = false,
                foodDosis = foodDosis,
                dosisCorrection = dosisCorrection,
                dosis = finalDosis
            )
        }
    }
}