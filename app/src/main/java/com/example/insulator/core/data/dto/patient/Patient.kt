package com.example.insulator.core.data.dto.patient

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatientResponse(
    @SerialName("parameters") val params: PatientParameters,
    @SerialName("patientId") val id : String,
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