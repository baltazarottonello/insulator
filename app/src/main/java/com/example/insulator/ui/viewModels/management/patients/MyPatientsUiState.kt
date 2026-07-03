package com.example.insulator.ui.viewModels.management.patients

import com.example.insulator.core.data.dto.patient.PatientCard

data class MyPatientsUiState(
    val patients: List<PatientCard> = listOf<PatientCard>(),
    val loading : Boolean = false,
    val error : String = "",
    val patientsLoaded : Boolean = false,
    val updating : Boolean = false,
    val updateSuccessful: Boolean = false
)
