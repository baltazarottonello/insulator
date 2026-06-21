package com.example.insulator.core.data.repositories.user

import com.example.insulator.core.data.dto.auth.LoginResponse
import com.example.insulator.core.data.repositories.tokens.TokenRepository
import com.example.insulator.core.services.auth.IAuthService
import com.example.insulator.ui.viewModels.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserRepository(private val authService: IAuthService, private val tokenRepository: TokenRepository) : IUserRepository {

    private val _userState = MutableStateFlow(UserState());
    override val userState: StateFlow<UserState> = _userState.asStateFlow();

    override suspend fun authenticateWithGoogle(idToken: String): Result<LoginResponse> {

        val result = authService.authenticateWithGoogle(idToken);

        result.onSuccess { data ->
            _userState.update {
                it.copy(
                    email = data.user.email,
                    avatar = data.user.avatar,
                    fullName = data.user.fullName,
                    roles = data.roles,
                    isLogged = true,
                    isPatient = data.roles.contains("PACIENTE")
                )
            }
            tokenRepository.saveTokens(
                accessToken = data.accessToken,
                refreshToken = data.refreshToken
            )
        }

        return result;
    }
}