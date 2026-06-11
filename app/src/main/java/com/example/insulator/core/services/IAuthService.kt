package com.example.insulator.core.services

import com.example.insulator.core.data.dto.LoginResponse

interface IAuthService {
    suspend fun authenticateWithGoogle(idToken: String): Result<LoginResponse>
}