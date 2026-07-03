package com.example.insulator.core.data.repositories.user

import com.example.insulator.core.data.dto.auth.LoginResponse
import com.example.insulator.ui.viewModels.UserState
import kotlinx.coroutines.flow.StateFlow

interface IUserRepository {
    suspend fun authenticateWithGoogle(idToken: String): Result<LoginResponse>
}