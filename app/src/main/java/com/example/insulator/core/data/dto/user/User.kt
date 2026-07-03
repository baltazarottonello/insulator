package com.example.insulator.core.data.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String,
    @SerialName("avatar") val avatar: String,
    @SerialName("fullName") val fullName: String
)