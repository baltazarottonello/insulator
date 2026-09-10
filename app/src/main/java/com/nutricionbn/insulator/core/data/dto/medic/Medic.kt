package com.nutricionbn.insulator.core.data.dto.medic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatientMedicCard(
    @SerialName("medic_id") val id: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("last_name") val lastName: String = "",
    @SerialName("email") val email: String = "",
    @SerialName("medic_since") val medicSince: String = ""
)

@Serializable
data class UnlinkMedicRequest(
    @SerialName("medic_id") val id: String = ""
)