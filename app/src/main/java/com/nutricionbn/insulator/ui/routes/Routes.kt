package com.nutricionbn.insulator.ui.routes

enum class AppRoutes(
    val showInTopBar: Boolean,
    val title: String? = null
) {
    Authentication(showInTopBar = false),
    Profile(showInTopBar = true, title = "Perfil"),
    Calculation(showInTopBar = true, title = "Insulator"),
    Management(showInTopBar = true, title = "Gestión"),
    PatientsManagement(showInTopBar = true, title = "Gestión"),
    PatientAssignment(showInTopBar = true, title = "Gestión"),
    MyPatients(showInTopBar = true, title = "Mis Pacientes");

    companion object {
        fun fromRoute(route: String?): AppRoutes? {
            return entries.find { it.name == route }
        }
    }
}