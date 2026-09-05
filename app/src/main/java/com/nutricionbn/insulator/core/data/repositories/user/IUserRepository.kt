package com.nutricionbn.insulator.core.data.repositories.user

import com.nutricionbn.insulator.core.data.dto.auth.LoginResponse
import com.nutricionbn.insulator.core.data.dto.user.AssignableUserResponse
import com.nutricionbn.insulator.core.data.dto.user.DeleteUserDataRequest
import com.nutricionbn.insulator.core.data.dto.user.UpdateUserProfileDataRequest
import com.nutricionbn.insulator.ui.viewModels.management.patients.AssignableUser

interface IUserRepository {
    suspend fun authenticateWithGoogle(idToken: String): Result<LoginResponse>
    suspend fun searchUserByEmail(email: String): Result<AssignableUserResponse>
    suspend fun assignAsAPatient(user: AssignableUser): Result<Any>
    suspend fun updateProfileData(data: UpdateUserProfileDataRequest): Result<Any>
    suspend fun deleteUserData(): Result<Any>

}