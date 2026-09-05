package com.nutricionbn.insulator.core.data.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("avatar") val avatar: String,
    @SerialName("fullName") val fullName: String,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,

    )

@Serializable
data class DeleteUserDataRequest(@SerialName("id") val id: String)
@Serializable
data class UpdateUserProfileDataRequest(
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
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

@Serializable
data class DoseUploadRequest(
    val bloodGlucose: Float,
    val carbs: Float,
    val ratio: Float,
    val sensitivity: Float,
    val targetGlucose: Float,
    val doseCorrection: Float,
    val foodDose: Float,
    val dose: Float,
    val observations: String?,
    val createdAt: String
)
