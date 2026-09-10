package com.nutricionbn.insulator.ui.viewModels.management.medics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutricionbn.insulator.core.data.dto.patient.PatientParameters
import com.nutricionbn.insulator.core.data.repositories.patients.PatientRepository
import com.nutricionbn.insulator.core.data.repositories.user.SessionRepository
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

    private val _myPatientsUiState = MutableStateFlow(MyPatientsUiState())
    val myPatientsUiState: StateFlow<MyPatientsUiState> = _myPatientsUiState.asStateFlow()
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

    fun dismissSuccessMessage(updateState: (MyPatientsUiState) -> MyPatientsUiState) {
        _myPatientsUiState.update(updateState)
    }

    fun loadPatients(id: String) {
        viewModelScope.launch {
            _myPatientsUiState.update { it.copy(loadingPatients = true, error = "") }
            _patientRepository.getPatients(id).onSuccess { data ->
                _myPatientsUiState.update {
                    it.copy(
                        patients = data,
                        patientsLoaded = true,
                        loadingPatients = false,
                        error = ""
                    )
                }
            }.onFailure { error ->
                _myPatientsUiState.update {
                    it.copy(
                        loadingPatients = false,
                        error = error.message!!
                    )
                }
            }
        }
    }

    fun loadParamsHistory(patientId: String) {
        viewModelScope.launch {
            _myPatientsUiState.update { currentState ->
                val existingList = currentState.paramsHistoryData.toMutableList()
                val index = existingList.indexOfFirst { it.patientId == patientId }

                val loadingState = if (index != -1) {
                    existingList[index].copy(isLoading = true, error = "")
                } else {
                    PatientParamsHistoryState(patientId = patientId, isLoading = true, error = "")
                }

                if (index != -1) {
                    existingList[index] = loadingState
                } else {
                    existingList.add(loadingState)
                }

                currentState.copy(paramsHistoryData = existingList)
            }


            _patientRepository.getPatientParamsHistory(patientId).onSuccess { data ->
                val updatedParamsHistoryState = PatientParamsHistoryState(
                    patientId = patientId,
                    data = data.history,
                    isLoading = false,
                    error = ""
                )

                _myPatientsUiState.update { currentState ->
                    val existingList = currentState.paramsHistoryData.toMutableList()
                    val index = existingList.indexOfFirst { it.patientId == patientId }

                    if (index != -1) {
                        existingList[index] = updatedParamsHistoryState
                    } else {
                        existingList.add(updatedParamsHistoryState)
                    }

                    currentState.copy(paramsHistoryData = existingList)
                }
            }.onFailure { error ->
                val errorMessage = error.message ?: "Error desconocido"

                _myPatientsUiState.update { currentState ->
                    val existingList = currentState.paramsHistoryData.toMutableList()
                    val index = existingList.indexOfFirst { it.patientId == patientId }

                    val errorState = if (index != -1) {
                        existingList[index].copy(isLoading = false, error = errorMessage)
                    } else {
                        PatientParamsHistoryState(
                            patientId = patientId,
                            isLoading = false,
                            error = errorMessage
                        )
                    }

                    if (index != -1) {
                        existingList[index] = errorState
                    } else {
                        existingList.add(errorState)
                    }

                    currentState.copy(paramsHistoryData = existingList)
                }
            }
        }
    }

    fun loadDosesHistory(patientId: String) {
        viewModelScope.launch {
            _myPatientsUiState.update { currentState ->
                val existingList = currentState.dosesHistoryData.toMutableList()
                val index = existingList.indexOfFirst { it.patientId == patientId }

                val loadingState = if (index != -1) {
                    existingList[index].copy(isLoading = true, error = "")
                } else {
                    PatientDosesHistoryState(patientId = patientId, isLoading = true, error = "")
                }

                if (index != -1) existingList[index] = loadingState else existingList.add(
                    loadingState
                )
                currentState.copy(dosesHistoryData = existingList)
            }

            _patientRepository.getPatientDosesHistory(patientId).onSuccess { data ->
                _myPatientsUiState.update { currentState ->
                    val existingList = currentState.dosesHistoryData.toMutableList()
                    val index = existingList.indexOfFirst { it.patientId == patientId }

                    val successState = PatientDosesHistoryState(
                        patientId = patientId,
                        data = data.history,
                        isLoading = false,
                        error = ""
                    )

                    if (index != -1) existingList[index] = successState else existingList.add(
                        successState
                    )
                    currentState.copy(dosesHistoryData = existingList)
                }
            }.onFailure { error ->
                _myPatientsUiState.update { currentState ->
                    val existingList = currentState.dosesHistoryData.toMutableList()
                    val index = existingList.indexOfFirst { it.patientId == patientId }

                    val errorState = if (index != -1) {
                        existingList[index].copy(
                            isLoading = false,
                            error = error.message ?: "Error"
                        )
                    } else {
                        PatientDosesHistoryState(
                            patientId = patientId,
                            isLoading = false,
                            error = error.message ?: "Error"
                        )
                    }

                    if (index != -1) existingList[index] = errorState else existingList.add(
                        errorState
                    )
                    currentState.copy(dosesHistoryData = existingList)
                }
            }
        }
    }

    fun updatePatientParams(patient: PatientParams) {
        viewModelScope.launch {
            _myPatientsUiState.update {
                it.copy(
                    updatingParams = true,
                    error = "",
                    paramsUpdateSuccessful = false
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
                    val updatedPatients = it.patients.map { patientCard ->
                        if (patientCard.id == patient.id) {
                            patientCard.copy(
                                ratio = patient.ratio.toFloat(),
                                sensitivity = patient.sensitivity.toFloat(),
                                targetGlucose = patient.targetGlucose.toFloat()
                            )
                        } else {
                            patientCard
                        }
                    }

                    it.copy(
                        updatingParams = false,
                        paramsUpdateSuccessful = true,
                        error = "", patients = updatedPatients
                    )
                }
            }.onFailure { error ->
                _myPatientsUiState.update {
                    it.copy(
                        updatingParams = false,
                        paramsUpdateSuccessful = false,
                        error = error.message!!
                    )
                }
            }
        }
    }
}