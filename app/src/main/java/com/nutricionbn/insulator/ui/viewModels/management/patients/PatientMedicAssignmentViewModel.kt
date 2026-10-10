package com.nutricionbn.insulator.ui.viewModels.management.patients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutricionbn.insulator.core.data.dto.medic.UnlinkMedicRequest
import com.nutricionbn.insulator.core.data.repositories.medics.MedicRepository
import com.nutricionbn.insulator.core.data.repositories.user.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PatientMedicAssignmentViewModel(
    private val _medicRepository: MedicRepository,
    private val _sessionRepository: SessionRepository
) : ViewModel() {
    private val _patientMedicAssignmentUiState = MutableStateFlow(
        PatientMedicAssignmentUiState()
    )
    val patientMedicAssignmentUiState =
        _patientMedicAssignmentUiState as StateFlow<PatientMedicAssignmentUiState>

    init {
        viewModelScope.launch {
            loadMedics()
        }
    }

    fun loadMedics() {
        viewModelScope.launch {
            _patientMedicAssignmentUiState.update { it.copy(loading = true, error = "") }
            _medicRepository.getMedics().onSuccess { data ->
                _patientMedicAssignmentUiState.update {
                    it.copy(
                        medics = data,
                        loading = false,
                        error = ""
                    )
                }
            }.onFailure { error ->
                _patientMedicAssignmentUiState.update {
                    it.copy(
                        loading = false,
                        error = error.message!!
                    )
                }
            }
        }
    }

    fun onNavigationHandled() {
        _patientMedicAssignmentUiState.update {
            it.copy(shouldNavigateAway = false)
        }
    }

    fun onUnlinkMedicClick() {
        _patientMedicAssignmentUiState.update {
            it.copy(
                error = "",
                showUnlinkMedicConfirmationModal = true
            )
        }
    }

    fun onUnlinkMedicConfirmation(medicId: String) {
        viewModelScope.launch {
            _patientMedicAssignmentUiState.update { it.copy(modalLoading = true) }
            _medicRepository.unlinkMedic(UnlinkMedicRequest(medicId)).onSuccess {
                val newMedicsList =
                    _patientMedicAssignmentUiState.value.medics.filterNot { it.id == medicId }
                _patientMedicAssignmentUiState.update {
                    it.copy(
                        medics = newMedicsList,
                        error = "",
                        showUnlinkMedicSuccessModal = true,
                        showUnlinkMedicConfirmationModal = false,
                        modalLoading = false
                    )
                }
                if (_patientMedicAssignmentUiState.value.medics.isEmpty()) {
                    val actualState = _sessionRepository.userState
                    val newRoles = actualState.value.roles.filterNot { it == "PACIENTE" }
                    _sessionRepository.updateState(actualState.value.copy(roles = newRoles))
                    _patientMedicAssignmentUiState.update { it.copy(shouldNavigateAway = true) }
                }
            }
                .onFailure { error ->
                    _patientMedicAssignmentUiState.update {
                        it.copy(
                            error = error.message!!,
                            showUnlinkMedicConfirmationModal = false,
                            modalLoading = false
                        )
                    }
                }
        }
    }

    fun onUnlinkMedicDismiss() {
        _patientMedicAssignmentUiState.update {
            it.copy(
                showUnlinkMedicConfirmationModal = false
            )
        }
    }

    fun onUnlinkMedicSuccessDismiss() {
        _patientMedicAssignmentUiState.update {
            it.copy(
                showUnlinkMedicSuccessModal = false
            )
        }
    }

    fun onErrorDialogDismissed() {
        _patientMedicAssignmentUiState.update { it.copy(error = "") }
    }
}