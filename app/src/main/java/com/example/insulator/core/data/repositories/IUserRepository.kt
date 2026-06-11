package com.example.insulator.core.data.repositories

import com.example.insulator.core.data.dto.LoginResponse
import kotlinx.coroutines.flow.StateFlow

interface IUserRepository {
    suspend fun authenticateWithGoogle(idToken: String): Result<LoginResponse>

    val userState: StateFlow<UserState>
}