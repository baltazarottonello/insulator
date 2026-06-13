package com.example.insulator.ui.viewModels.management.patients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insulator.core.data.dto.patient.PatientParameters
import com.example.insulator.core.data.repositories.patients.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PatientManagementViewModel(private val patientRepository: PatientRepository) : ViewModel() {

    val patientState: StateFlow<PatientState> = patientRepository.patientState;

    private val _patientManagementUiState = MutableStateFlow(
        PatientManagementUiState()
    )

    val patientManagementUiState = _patientManagementUiState as StateFlow<PatientManagementUiState>

    fun setLoading(loading: Boolean) {
        _patientManagementUiState.update { it.copy(isLoading = loading) }
    }

    private val _uiState: StateFlow<PatientManagementUiState> = patientState
        .map { patient ->
            PatientManagementUiState()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PatientManagementUiState()
        )

    fun onSearchPatientByEmail(email: String) {
        viewModelScope.launch {
            _patientManagementUiState.update { it.copy(isLoading = true, error = null) }
            try {
                patientRepository.searchPatientByEmail(email)
                _patientManagementUiState.update {
                    it.copy(
                        isLoading = false,
                        patientLoaded = true,
                    )
                }
            } catch (e: Exception) {
                _patientManagementUiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun onSetPatientParams(id: String, params: PatientParameters) {
        viewModelScope.launch {
            _patientManagementUiState.update { it.copy(isLoading = true, error = "", isSuccess = false) }
            patientRepository.setPatientParameters(id, params)
                .onSuccess {
                    _patientManagementUiState.update { it.copy(isSuccess = true, isLoading = false, patientLoaded = false) }
                }
                .onFailure { error ->
                    _patientManagementUiState.update { it.copy(isSuccess = false, error = error.message) }
                }
        }
    }
}