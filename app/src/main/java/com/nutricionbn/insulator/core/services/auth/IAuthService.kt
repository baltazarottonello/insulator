package com.nutricionbn.insulator.core.services.auth

import com.nutricionbn.insulator.core.data.dto.auth.DemoUserCredentials
import com.nutricionbn.insulator.core.data.dto.auth.LoginResponse

interface IAuthService {
    suspend fun authenticateWithGoogle(idToken: String): Result<LoginResponse>
    suspend fun authenticateForDemo(credentials: DemoUserCredentials): Result<LoginResponse>
}
