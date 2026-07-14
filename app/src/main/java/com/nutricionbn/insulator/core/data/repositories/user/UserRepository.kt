package com.nutricionbn.insulator.core.data.repositories.user

import com.nutricionbn.insulator.core.data.dto.auth.LoginResponse
import com.nutricionbn.insulator.core.data.dto.user.AssignableUserResponse
import com.nutricionbn.insulator.core.data.repositories.tokens.TokenRepository
import com.nutricionbn.insulator.core.services.auth.IAuthService
import com.nutricionbn.insulator.core.services.user.UserService
import com.nutricionbn.insulator.ui.viewModels.UserState
import com.nutricionbn.insulator.ui.viewModels.management.patients.AssignableUser

class UserRepository(
    private val _authService: IAuthService,
    private val _tokenRepository: TokenRepository,
    private val _sessionRepository: SessionRepository,
    private val _userService: UserService,
) : IUserRepository {

    override suspend fun authenticateWithGoogle(idToken: String): Result<LoginResponse> {

        val result = _authService.authenticateWithGoogle(idToken);

        result.onSuccess { data ->
            _sessionRepository.updateState(
                UserState(
                    id = data.user.id,
                    email = data.user.email,
                    avatar = data.user.avatar,
                    fullName = data.user.fullName,
                    roles = data.roles,
                    isLogged = true,
                    isPatient = data.roles.contains("PACIENTE")
                )
            )
            _tokenRepository.saveTokens(
                accessToken = data.accessToken,
                refreshToken = data.refreshToken
            )
        }

        return result;
    }

    override suspend fun searchUserByEmail(email: String): Result<AssignableUserResponse> {
        val result = _userService.checkAvailability(email)
        val user: AssignableUserResponse? = result.getOrNull()
        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("El usuario es paciente de otro médico o no existe"))
        }
    }

    override suspend fun assignAsAPatient(user: AssignableUser): Result<Any> {
        val hasBeenPatient = !user.patientId.isNullOrEmpty()
        return _userService.assignUserAsAPatient(user.id, hasBeenPatient);
    }
}