package com.nutricionbn.insulator.ui.viewModels.calculation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutricionbn.insulator.core.data.repositories.calculation.CalculationSnapshotRepository
import com.nutricionbn.insulator.core.data.repositories.calculation.CalculatorPreferencesRepository
import com.nutricionbn.insulator.core.data.repositories.calculation.DoseCalculationSnapshot
import com.nutricionbn.insulator.core.data.repositories.patients.PatientRepository
import com.nutricionbn.insulator.core.data.repositories.user.SessionRepository
import com.nutricionbn.insulator.core.data.source.FoodDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.round
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CalculationViewModel(
    private val _foodDataSource: FoodDataSource,
    private val _preferencesRepository: CalculatorPreferencesRepository,
    private val _sessionRepository: SessionRepository,
    private val _patientRepository: PatientRepository,
    private val _calcSnapshotRepository: CalculationSnapshotRepository
) : ViewModel() {
    private val _uiState =
        MutableStateFlow(
            CalculatorParamsUiState(
                foods = _foodDataSource.getAvailableFoods(),
                isPatient = _sessionRepository.userState.value.isPatient,
                lastDoseCalculationSnapshot = DoseCalculationSnapshot()
            )
        )
    val uiState: StateFlow<CalculatorParamsUiState> = _uiState.asStateFlow()

    val availableFoods = _foodDataSource.getAvailableFoods()
    private val _foodList = MutableStateFlow(availableFoods)
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val filteredList = combine(_searchQuery, _foodList) { query, list ->
        if (query.isEmpty()) {
            list
        } else {
            list.filter { it.name.contains(query, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _foodList.value)

    init {
        viewModelScope.launch {
            try {
                val snapshot = _calcSnapshotRepository.getCalculationSnapshot()
                _uiState.update {
                    it.copy(
                        lastDoseCalculationSnapshot = snapshot,
                        showPendingSnapshotConfirmationDialog = !snapshot.isLoaded
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al obtener datos del almacenamiento interno") }
            }
        }

        //PARÁMETROS QUE SIEMPRE SE CARGAN DE PREFERENCIAS
        viewModelScope.launch {
            _preferencesRepository.userPrefsFlow.collectLatest { prefs ->
                _uiState.update {
                    it.copy(
                        ratio = prefs.ratio,
                        carbs = prefs.carbs,
                    )
                }
            }
        }

        viewModelScope.launch {
            _sessionRepository.userState
                .collectLatest { user ->
                    _uiState.update { it.copy(isLoading = true) }
                    if (user.isLogged && user.isPatient) {
                        _patientRepository.getParameters().onSuccess { response ->
                            _uiState.update {
                                //PARÁMETROS CARGADOS POR EL MÉDICO
                                it.copy(
                                    sensitivity = response.params.sens.toString(),
                                    targetGlucose = response.params.targetGlucose.toString(),
                                    isPatient = true,
                                    isLoading = false
                                )
                            }
                        }.onFailure {
                            _uiState.update {
                                it.copy(
                                    getParamsError = "Tu médico aún no cargó tus parámetros",
                                    isLoading = false
                                )
                            }
                        }
                    } else {
                        //PARÁMETROS CARGADOS DESDE PREFERENCIAS SOLO SI ES USUARIO NORMAL
                        _preferencesRepository.userPrefsFlow.collectLatest { prefs ->
                            _uiState.update {
                                it.copy(
                                    sensitivity = prefs.sensitivity,
                                    targetGlucose = prefs.targetGlucose,
                                    isPatient = false,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }

        }
    }

    //HANDLERS
    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onGlucoseChanged(newValue: String) {
        if (_uiState.value.showConfirmationButton) {
            _uiState.update { it.copy(showUnsavedWarningDialog = true) }
            return
        }
        _uiState.update { currentState ->
            currentState.copy(
                glucose = newValue,
            )
        }
    }

    fun onCarbsChanged(newValue: String) {
        if (_uiState.value.showConfirmationButton) {
            _uiState.update { it.copy(showUnsavedWarningDialog = true) }
            return
        }
        _uiState.update { currentState ->
            currentState.copy(
                carbs = newValue,
            )
        }
        viewModelScope.launch {
            _preferencesRepository.saveCarbs(newValue)
        }
    }

    fun cleanAutoCalculatedParams() {
        if (_uiState.value.showConfirmationButton) {
            _uiState.update { it.copy(showUnsavedWarningDialog = true) }
            return
        }
        onCarbsChanged("")
    }

    fun addCarbsFromFood(food: Food) {
        if (_uiState.value.showConfirmationButton) {
            _uiState.update { it.copy(showUnsavedWarningDialog = true) }
            return
        }
        val currentCarbs = _uiState.value.carbs.toIntOrNull() ?: 0
        val newCarbs = currentCarbs + food.carbsValue
        onCarbsChanged(newCarbs.toString())
    }

    fun onRatioChanged(newValue: String) {
        if (_uiState.value.showConfirmationButton) {
            _uiState.update { it.copy(showUnsavedWarningDialog = true) }
            return
        }
        _uiState.update { it.copy(ratio = newValue) }
        viewModelScope.launch {
            _preferencesRepository.saveRatio(newValue)
        }
    }

    fun onSensitivityChanged(newValue: String) {
        if (_uiState.value.showConfirmationButton) {
            _uiState.update { it.copy(showUnsavedWarningDialog = true) }
            return
        }
        _uiState.update { it.copy(sensitivity = newValue) }
        viewModelScope.launch {
            _preferencesRepository.saveSensitivity(newValue)
        }
    }

    fun onTargetGlucoseChanged(newValue: String) {
        if (_uiState.value.showConfirmationButton) {
            _uiState.update { it.copy(showUnsavedWarningDialog = true) }
            return
        }
        _uiState.update { it.copy(targetGlucose = newValue) }
        viewModelScope.launch {
            _preferencesRepository.saveTargetGlucose(newValue)
        }
    }

    fun onErrorDialogDismissed() {
        _uiState.update { it.copy(error = "") }
    }

    fun dismissErrorMessage(updateState: (CalculatorParamsUiState) -> CalculatorParamsUiState) {
        _uiState.update(updateState)
    }

    fun onWarningDialogDismissed() {
        _uiState.update { it.copy(showUnsavedWarningDialog = false) }
        onDiscardDose()
    }

    fun onDiscardDose() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    showConfirmationButton = false,
                    showUnsavedWarningDialog = false,
                    error = ""
                )
            }
            try {
                _calcSnapshotRepository.discardSnapshot()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al descartar dosis pendiente de confirmación") }
            }
        }

    }

    fun onCalculateClicked() {
        _uiState.update {
            it.copy(
                loadingConfirmationSuccess = false,
                showDoseCalculationConfirmationDialog = false,
                showDoseResultCard = false
            )
        }
        val invalidCalculationData = validateDosisCalculationValues()

        if (invalidCalculationData) {
            return
        }

        calculateDosis()

        _uiState.update {
            it.copy(
                showDoseResultCard = true
            )
        }

        if (uiState.value.isPatient) {
            viewModelScope.launch {
                try {
                    _calcSnapshotRepository.saveDoseCalculationSnapshot(
                        makeDoseCalculationSnapshot()
                    )
                    _uiState.update { it.copy(showConfirmationButton = true) }
                } catch (e: Exception) {
                    _uiState.update { it.copy(error = "Error al cargar datos en el almacenamiento interno") }
                }
            }
        }
    }

    fun onConfirmationClicked() {
        _uiState.update { it.copy(showDoseCalculationConfirmationDialog = true) }
    }

    fun onConfirmationDismissed() {
        _uiState.update { it.copy(showDoseCalculationConfirmationDialog = false) }
        onDiscardDose()
    }

    fun onConfirmationConfirmed(observations: String?) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingConfirmation = true,
                    loadingConfirmationError = "",
                    showUnsavedWarningDialog = false
                )
            }
            _calcSnapshotRepository.uploadDoseCalculation(observations).onSuccess {
                _uiState.update {
                    it.copy(
                        showConfirmationButton = false,
                        loadingConfirmation = false,
                        loadingConfirmationSuccess = true,
                        loadingConfirmationError = "",
                        showUnsavedWarningDialog = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        loadingConfirmation = false,
                        loadingConfirmationSuccess = false,
                        showUnsavedWarningDialog = false,
                        loadingConfirmationError = error.message!!
                    )
                }
            }
        }
    }

    fun onPendingSnapshotConfirmation(observations: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingPendingSnapshot = true, error = "") }
            _calcSnapshotRepository.uploadDoseCalculation(observations).onSuccess {
                _uiState.update {
                    it.copy(
                        loadingPendingSnapshot = false,
                        showPendingSnapshotConfirmationDialog = false
                    )
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        error = "Error al confirmar dosis"
                    )
                }
            }
        }
    }

    fun dismissSuccessMessage(updateState: (CalculatorParamsUiState) -> CalculatorParamsUiState) {
        _uiState.update(updateState)
    }

    fun onDiscardPendingSnapshot() {
        viewModelScope.launch {
            _uiState.update { it.copy(showPendingSnapshotConfirmationDialog = false, error = "") }
            try {
                _calcSnapshotRepository.discardSnapshot()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al descartar dosis pendiente de confirmación") }
            }
        }
    }


    //UTILS
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


    fun validateDosisCalculationValues(): Boolean {
        val state = _uiState.value

        val isGlucoseEmpty = state.glucose.isEmpty()
        val isCarbsEmpty = state.carbs.isEmpty()
        val isRatioEmpty = state.ratio.isEmpty()
        val isSensEmpty = state.sensitivity.isEmpty()
        val isTargetGlucoseEmpty = state.targetGlucose.isEmpty()

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
            return true
        }

        val glucose = state.glucose.toFloatOrNull() ?: 0f
        val carbs = state.carbs.toFloatOrNull() ?: 0f
        val targetGlucose = state.targetGlucose.toFloatOrNull() ?: 0f
        val ratio = state.ratio.toFloatOrNull() ?: 0f
        val sensitivity = state.sensitivity.toFloatOrNull() ?: 0f

        val minBloodGlucose = 40f
        val maxBloodGlucose = 600f
        val minTargetGlucose = 70f
        val maxTargetGlucose = 250f
        val maxSafeCarbs = 300f


        val isBloodGlucoseUnsafe = glucose !in minBloodGlucose..maxBloodGlucose
        val isTargetGlucoseUnsafe = targetGlucose !in minTargetGlucose..maxTargetGlucose
        val isCarbsInvalid = carbs !in 0f..maxSafeCarbs
        val isRatioInvalid = ratio <= 0f || ratio > 100f
        val isSensInvalid = sensitivity <= 0f || sensitivity > 300f

        if (isBloodGlucoseUnsafe || isCarbsInvalid || isTargetGlucoseUnsafe || isRatioInvalid || isSensInvalid) {
            _uiState.update {
                it.copy(
                    formValidationError = true,
                    glucoseOutOfRange = isBloodGlucoseUnsafe,
                    carbsOutOfRange = isCarbsInvalid,
                    targetGlucoseOutOfRange = isTargetGlucoseUnsafe,
                    ratioOutOfRange = isRatioInvalid,
                    sensOutOfRange = isSensInvalid
                )
            }
            return true
        }

        _uiState.update {
            it.copy(
                formValidationError = false,
                showGlucoseError = false,
                showCarbsError = false,
                showRatioError = false,
                showSensError = false,
                showTargetGlucoseError = false,
                glucoseOutOfRange = false,
                carbsOutOfRange = false,
                targetGlucoseOutOfRange = false,
                ratioOutOfRange = false,
                sensOutOfRange = false
            )
        }

        return false
    }

    @OptIn(ExperimentalTime::class)
    fun makeDoseCalculationSnapshot(): DoseCalculationSnapshot {
        return DoseCalculationSnapshot(
            isLoaded = false,
            bloodGlucose = _uiState.value.glucose.toFloat(),
            carbs = _uiState.value.carbs.toFloat(),
            ratio = _uiState.value.ratio.toFloat(),
            sensitivity = _uiState.value.sensitivity.toFloat(),
            targetGlucose = _uiState.value.targetGlucose.toFloat(),
            doseCorrection = _uiState.value.dosisCorrection,
            foodDose = _uiState.value.foodDosis,
            dose = _uiState.value.dosis,
            calculatedAt = Clock.System.now().toString()
        )
    }
}