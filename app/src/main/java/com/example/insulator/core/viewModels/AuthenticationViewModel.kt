package com.example.insulator.core.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insulator.core.data.repositories.IUserRepository
import com.example.insulator.core.data.ui.AuthenticationUiState
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
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al sincronizar con el servidor"
                    )
                }
            }
        }
    }

    fun onSignInError(message: String) {
        _uiState.update { it.copy(error = message) }
    }
}