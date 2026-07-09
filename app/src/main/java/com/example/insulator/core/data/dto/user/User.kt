package com.example.insulator.core.data.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponse(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("avatar") val avatar: String,
    @SerialName("fullName") val fullName: String
)

@Serializable
data class AssignableUserResponse(
    @SerialName("user_id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("last_name") val lastName: String,
    @SerialName("email") val email: String,
    @SerialName("patient_id") val patientId: String?
)

@Serializable
data class AssignUserAsPatientRequest(
    val userId: String,
    val hasBeenPatient: Boolean,
)