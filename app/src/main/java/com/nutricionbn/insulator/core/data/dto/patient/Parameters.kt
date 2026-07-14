package com.nutricionbn.insulator.core.data.dto.patient

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatientParameters(
    @SerialName("ratio") val ratio: Float,
    @SerialName("sensitivity") val sens: Float,
    @SerialName("targetGlucose") val targetGlucose: Float
)

@Serializable
data class SetParametersRequest(
    @SerialName("patientId") val patientId: String,
    @SerialName("parameters") val params: PatientParameters,
)

@Serializable
data class ParametersResponse(
    @SerialName("parameters") val params: PatientParameters,
)