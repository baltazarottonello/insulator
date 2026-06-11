package com.example.insulator.core.data.repositories

import com.example.insulator.core.data.dto.LoginResponse
import com.example.insulator.core.services.IAuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserRepository(private val authService: IAuthService) : IUserRepository {

    private val _userState = MutableStateFlow(UserState());
    override val userState: StateFlow<UserState> = _userState.asStateFlow();

    override suspend fun authenticateWithGoogle(idToken: String): Result<LoginResponse> {

        val result = authService.authenticateWithGoogle(idToken);

        result.onSuccess { data ->
            _userState.update {
                it.copy(
                    id = data.user.id,
                    email = data.user.email,
                    avatar = data.user.avatar,
                    fullName = data.user.fullName,
                    accessToken = data.accessToken,
                    refreshToken = data.refreshToken,
                    isLogged = true
                )
            }
        }

        return result;
    }
}