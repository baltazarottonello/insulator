package com.example.insulator.core.data.repositories.user

import com.example.insulator.core.data.dto.auth.LoginResponse
import com.example.insulator.core.data.repositories.tokens.TokenRepository
import com.example.insulator.core.services.auth.IAuthService
import com.example.insulator.ui.viewModels.UserState

class UserRepository(
    private val authService: IAuthService,
    private val tokenRepository: TokenRepository,
    private val _sessionManager: SessionManager
) : IUserRepository {

    override suspend fun authenticateWithGoogle(idToken: String): Result<LoginResponse> {

        val result = authService.authenticateWithGoogle(idToken);

        result.onSuccess { data ->
            _sessionManager.updateState(
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
            tokenRepository.saveTokens(
                accessToken = data.accessToken,
                refreshToken = data.refreshToken
            )
        }

        return result;
    }
}