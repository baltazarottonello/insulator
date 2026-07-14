package com.nutricionbn.insulator.ui.viewModels.management.patients

data class SearchUserUiState(
    val userLoaded: Boolean = false,
    val user: AssignableUser = AssignableUser(),
    val isLoading: Boolean = false,
    val error: String? = "",
    val isSuccess: Boolean = false,
    val assigningPatient: Boolean = false,
)

data class AssignableUser(
    val id: String = "",
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val patientId: String? = null
)