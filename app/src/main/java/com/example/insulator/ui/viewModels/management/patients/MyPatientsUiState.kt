package com.example.insulator.ui.viewModels.management.patients

import com.example.insulator.core.data.dto.patient.PatientCard
import com.example.insulator.core.data.dto.patient.PatientParamsHistoryRow

data class MyPatientsUiState(
    val patients: List<PatientCard> = listOf<PatientCard>(),
    val loading: Boolean = false,
    val error: String = "",
    val patientsLoaded: Boolean = false,
    val updating: Boolean = false,
    val updateSuccessful: Boolean = false,
) {

}

data class PatientParamsHistoryState(
    val data: List<PatientParamsHistoryRow> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
