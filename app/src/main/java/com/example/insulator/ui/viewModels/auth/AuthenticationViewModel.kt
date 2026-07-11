package com.example.insulator.ui.viewModels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insulator.core.data.repositories.user.IUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val userRepository: IUserRepository) : ViewModel() {
    private val _uiState =
        MutableStateFlow(AuthenticationUiState())
    val uiState: StateFlow<AuthenticationUiState> = _uiState.asStateFlow()

    fun onGoogleSignInComplete(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = userRepository.authenticateWithGoogle(idToken)

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        interactionDisabled = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        interactionDisabled = false,
                        isLoading = false,
                        error = "Error al sincronizar con el servidor"
                    )
                }
            }
        }
    }

    fun onSignInError(message: String) {
        _uiState.update { it.copy(error = message, interactionDisabled = false) }
    }

    fun onGoogleSignInStarting() {
        _uiState.update { it.copy(interactionDisabled = true) }
    }
}