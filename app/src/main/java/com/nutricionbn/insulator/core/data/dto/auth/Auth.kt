package com.nutricionbn.insulator.core.data.dto.auth

import com.nutricionbn.insulator.core.data.dto.user.UserProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleLoginRequest(
    val token: String
)

@Serializable
data class GoogleLoginResponse(
    val sessionToken: String,
)


@Serializable
data class LoginResponse(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("user") val user: UserProfile,
    @SerialName("roles") val roles: List<String>
)
@Serializable
data class DemoUserCredentials(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
)