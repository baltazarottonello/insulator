package com.nutricionbn.insulator.ui.routes

enum class AppRoutes(
    val showInTopBar: Boolean,
    val title: String? = null
) {
    Authentication(showInTopBar = false),
    Profile(showInTopBar = true, title = "Perfil"),
    Calculation(showInTopBar = true, title = "Insulator"),

    //MEDICS MANAGEMENT
    MedicManagementDashboard(showInTopBar = true, title = "Gestión"),
    MedicPatientsManagement(showInTopBar = true, title = "Gestión"),
    MedicPatientAssignment(showInTopBar = true, title = "Gestión"),
    MedicPatients(showInTopBar = true, title = "Mis Pacientes"),

    //PATIENTS MANAGEMENT
    PatientManagementDashboard(showInTopBar = true, title = "Gestión"),
    PatientMedicsManagement(showInTopBar = true, title = "Gestión"),
    PatientMedicsAssignment(showInTopBar = true, title = "Mis Médicos");

    companion object {
        fun fromRoute(route: String?): AppRoutes? {
            return entries.find { it.name == route }
        }
    }
}