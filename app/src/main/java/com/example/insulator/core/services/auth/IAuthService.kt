package com.example.insulator.core.services.auth

import com.example.insulator.core.data.dto.auth.LoginResponse

interface IAuthService {
    suspend fun authenticateWithGoogle(idToken: String): Result<LoginResponse>
}