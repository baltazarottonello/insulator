package com.example.insulator.ui.viewModels.management.patients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insulator.core.data.dto.patient.PatientParameters
import com.example.insulator.core.data.repositories.patients.PatientRepository
import com.example.insulator.core.data.repositories.user.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyPatientsViewModel(
    private val _patientRepository: PatientRepository,
    private val _sessionRepository: SessionRepository
) : ViewModel() {

    private val _myPatientsUiState = MutableStateFlow(MyPatientsUiState());
    val myPatientsUiState: StateFlow<MyPatientsUiState> = _myPatientsUiState.asStateFlow();
    val userState = _sessionRepository.userState

    init {
        viewModelScope.launch {
            userState
                .map { it.id }
                .distinctUntilChanged()
                .collectLatest { id ->
                    if (id.isNotEmpty()) {
                        loadPatients(id)
                    }
                }
        }
    }

    fun loadPatients(id: String) {
        viewModelScope.launch {
            _myPatientsUiState.update { it.copy(loading = true, error = "") }
            _patientRepository.getPatients(id).onSuccess { data ->
                _myPatientsUiState.update {
                    it.copy(
                        patients = data,
                        patientsLoaded = true,
                        loading = false,
                        error = ""
                    )
                }
            }.onFailure { error ->
                _myPatientsUiState.update { it.copy(loading = false, error = error.message!!) }
            }
        }
    }

    fun updatePatientParams(patient: PatientParams) {
        viewModelScope.launch {
            _myPatientsUiState.update {
                it.copy(
                    updating = true,
                    error = "",
                    updateSuccessful = false
                )
            }
            _patientRepository.setPatientParameters(
                id = patient.id,
                params = PatientParameters(
                    ratio = patient.ratio.toFloat(),
                    sens = patient.sensitivity.toFloat(),
                    targetGlucose = patient.targetGlucose.toFloat()
                )
            ).onSuccess {
                _myPatientsUiState.update {
                    it.copy(
                        loading = false,
                        updateSuccessful = true,
                        error = ""
                    )
                }
            }.onFailure { error ->
                _myPatientsUiState.update {
                    it.copy(
                        loading = false,
                        updateSuccessful = false,
                        error = error.message!!
                    )
                }
            }
        }
    }
}