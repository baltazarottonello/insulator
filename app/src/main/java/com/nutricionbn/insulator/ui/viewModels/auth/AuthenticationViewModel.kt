package com.nutricionbn.insulator.ui.viewModels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutricionbn.insulator.core.data.repositories.user.IUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val _userRepository: IUserRepository) : ViewModel() {
    private val _uiState =
        MutableStateFlow(AuthenticationUiState())
    val uiState: StateFlow<AuthenticationUiState> = _uiState.asStateFlow()

    fun onGoogleSignInComplete(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = "") }

            _userRepository.authenticateWithGoogle(idToken).onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        interactionDisabled = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        interactionDisabled = false,
                        isLoading = false,
                        error = error.message!!
                    )
                }
            }
        }
    }

    fun onSignInError(message: String) {
        _uiState.update { it.copy(error = message, interactionDisabled = false) }
    }

    fun dismissErrorMessage(updateState: (AuthenticationUiState) -> AuthenticationUiState) {
        _uiState.update(updateState)
    }

    fun onGoogleSignInStarting() {
        _uiState.update { it.copy(interactionDisabled = true) }
    }
}