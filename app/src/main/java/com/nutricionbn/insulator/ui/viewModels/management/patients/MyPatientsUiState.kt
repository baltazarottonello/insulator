package com.nutricionbn.insulator.ui.viewModels.management.patients

import com.nutricionbn.insulator.core.data.dto.patient.PatientCard
import com.nutricionbn.insulator.core.data.dto.patient.PatientDoseHistoryRow
import com.nutricionbn.insulator.core.data.dto.patient.PatientParamsHistoryRow

data class MyPatientsUiState(
    val patients: List<PatientCard> = listOf<PatientCard>(),
    val paramsHistoryData: List<PatientParamsHistoryState> = emptyList(),
    val dosesHistoryData: List<PatientDosesHistoryState> = emptyList(),
    val error: String = "",
    val loadingPatients: Boolean = false,
    val showDoseObservationsModal: Boolean = false,
    val patientsLoaded: Boolean = false,
    val updatingParams: Boolean = false,
    val paramsUpdateSuccessful: Boolean = false,
) {

}

data class PatientParamsHistoryState(
    val patientId: String = "",
    val data: List<PatientParamsHistoryRow> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)

data class PatientDosesHistoryState(
    val patientId: String = "",
    val data: List<PatientDoseHistoryRow> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
