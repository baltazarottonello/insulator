package com.nutricionbn.insulator.ui.viewModels

data class UserState(
    val id: String = "",
    val email: String = "",
    val avatar: String = "",
    val fullName: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val roles: List<String> = listOf(),
    val isLogged: Boolean = false,
) {
    val isPatient: Boolean
        get() = roles.contains("PACIENTE")
    val isMedic: Boolean
        get() = roles.contains("MÉDICO")
    val isAdmin: Boolean
        get() = roles.contains("ADMINISTRADOR")
}