package com.example.insulator.ui.viewModels.management.patients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insulator.core.data.dto.patient.PatientParameters
import com.example.insulator.core.data.repositories.patients.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PatientManagementViewModel(private val patientRepository: PatientRepository) : ViewModel() {

    private val _patientState = MutableStateFlow(PatientState());
    val patientState: StateFlow<PatientState> = _patientState.asStateFlow();

    private val _patientManagementUiState = MutableStateFlow(
        PatientManagementUiState()
    )
    val patientManagementUiState = _patientManagementUiState as StateFlow<PatientManagementUiState>
    fun onSetPatientParams(id: String, params: PatientParameters) {
        viewModelScope.launch {
            _patientManagementUiState.update {
                it.copy(
                    isLoading = true,
                    error = "",
                    isSuccess = false
                )
            }
            patientRepository.setPatientParameters(id, params)
                .onSuccess {
                    _patientManagementUiState.update {
                        it.copy(
                            isSuccess = true,
                            isLoading = false,
                            patientLoaded = false
                        )
                    }
                }
                .onFailure { error ->
                    _patientManagementUiState.update {
                        it.copy(
                            isSuccess = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    fun onSearchPatientByEmail(email: String) {
        viewModelScope.launch {
            _patientManagementUiState.update { it.copy(isLoading = true, error = null) }
            patientRepository.searchPatientByEmail(email).onSuccess { payload ->
                _patientState.update {
                    it.copy(
                        id = payload.id,
                        ratio = payload.params.ratio.toString(),
                        sensitivity = payload.params.sens.toString(),
                        targetGlucose = payload.params.targetGlucose.toString()
                    )
                }
                _patientManagementUiState.update {
                    it.copy(
                        isLoading = false,
                        patientLoaded = true,
                    )
                }
                _patientManagementUiState.update { it.copy(isLoading = false, error = "") }
            }.onFailure { error ->
                _patientManagementUiState.update {
                    it.copy(
                        error = error.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onRatioChange(value: String) {
        _patientState.update { it.copy(ratio = value) }
    }

    fun onSensChange(value: String) {
        _patientState.update { it.copy(sensitivity = value) }
    }

    fun onTargetGlucoseChange(value: String) {
        _patientState.update { it.copy(targetGlucose = value) }
    }
}