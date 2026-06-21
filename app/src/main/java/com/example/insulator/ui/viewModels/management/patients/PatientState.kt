package com.example.insulator.ui.viewModels.management.patients

data class PatientState(
    val id: String = "",
    val ratio: Float = 0f,
    val sensitivity: Float = 0f,
    val targetGlucose: Float = 0f,
)
