package com.nutricionbn.insulator.core.data.dto.patient

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatientResponse(
    @SerialName("parameters") val params: PatientParameters,
    @SerialName("patientId") val id: String,
)


@Serializable
data class PatientCard(
    @SerialName("patient_id") val id: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("last_name") val lastName: String = "",
    @SerialName("email") val email: String = "",
    @SerialName("sensitivity") val sensitivity: Float = 0f,
    @SerialName("ratio") val ratio: Float = 0f,
    @SerialName("target_glucose") val targetGlucose: Float = 0f,
    @SerialName("created_at") val createdAt: String = ""
)

@Serializable
data class PatientParamsHistoryResponse(
    @SerialName("patientId") val patientId: String = "",
    @SerialName("history") val history: List<PatientParamsHistoryRow>,
)

@Serializable
data class PatientParamsHistoryRow(
    @SerialName("sensitivity") val sensitivity: Float = 0f,
    @SerialName("ratio") val ratio: Float = 0f,
    @SerialName("targetGlucose") val targetGlucose: Float = 0f,
    @SerialName("createdAt") val createdAt: String = "",
    @SerialName("isActive") val isActive: Boolean = false,
)

@Serializable
data class PatientDosesHistoryResponse(
    @SerialName("userId") val userId: String = "",
    @SerialName("patientId") val patientId: String = "",
    @SerialName("history") val history: List<PatientDoseHistoryRow>,
)

@Serializable
data class PatientDoseHistoryRow(
    @SerialName("bloodGlucose") val bloodGlucose: Float = 0f,
    @SerialName("carbs") val carbs: Float = 0f,
    @SerialName("ratio") val ratio: Float = 0f,
    @SerialName("sensitivity") val sensitivity: Float = 0f,
    @SerialName("targetGlucose") val targetGlucose: Float = 0f,
    @SerialName("doseCorrection") val doseCorrection: Float = 0f,
    @SerialName("foodDose") val foodDose: Float = 0f,
    @SerialName("dose") val dose: Float = 0f,
    @SerialName("observations") val observations: String? = null,
    @SerialName("createdAt") val createdAt: String = "",
)