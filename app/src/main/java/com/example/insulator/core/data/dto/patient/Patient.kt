package com.example.insulator.core.data.dto.patient

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatientResponse(
    @SerialName("parameters") val params: PatientParameters,
    @SerialName("patientId") val id : String,
)