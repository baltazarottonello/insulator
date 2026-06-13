package com.example.insulator.ui.viewModels.management.patients

data class PatientManagementUiState(
    val patientLoaded: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = "",
    val isSuccess : Boolean = false,
)