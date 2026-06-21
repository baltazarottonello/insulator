package com.example.insulator.ui.viewModels

data class UserState(
    val id: String = "",
    val email: String = "",
    val avatar: String = "",
    val fullName: String = "",
    val roles: List<String> = listOf(),
    val isLogged: Boolean = false,
    val isPatient: Boolean = false,
)