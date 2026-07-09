package com.example.insulator.core.data.repositories.user

import com.example.insulator.core.data.dto.auth.LoginResponse
import com.example.insulator.core.data.dto.user.AssignableUserResponse
import com.example.insulator.ui.viewModels.management.patients.AssignableUser

interface IUserRepository {
    suspend fun authenticateWithGoogle(idToken: String): Result<LoginResponse>
    suspend fun searchUserByEmail(email: String): Result<AssignableUserResponse>
    suspend fun assignAsAPatient(user: AssignableUser): Result<Any>

}