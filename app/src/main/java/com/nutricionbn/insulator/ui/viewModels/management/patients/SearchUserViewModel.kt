package com.nutricionbn.insulator.ui.viewModels.management.patients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutricionbn.insulator.core.data.repositories.user.SessionRepository
import com.nutricionbn.insulator.core.data.repositories.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchUserViewModel(
    private val _userRepository: UserRepository,
    private val _sessionRepository: SessionRepository
) : ViewModel() {

    private val _searchUserUiState = MutableStateFlow(
        SearchUserUiState()
    )
    val searchUserUiState = _searchUserUiState as StateFlow<SearchUserUiState>
    val userState = _sessionRepository.userState

    fun onSearchUserByEmail(email: String) {
        viewModelScope.launch {
            _searchUserUiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    userLoaded = false,
                )
            }
            if (email != userState.value.email) {
                _userRepository.searchUserByEmail(email).onSuccess { payload ->
                    _searchUserUiState.update {
                        it.copy(
                            user = AssignableUser(
                                id = payload.id,
                                name = payload.name,
                                email = payload.email,
                                lastName = payload.lastName,
                                patientId = payload.patientId
                            ),
                            isLoading = false,
                            userLoaded = true,
                        )
                    }
                    _searchUserUiState.update { it.copy(isLoading = false, error = "") }
                }.onFailure { error ->
                    _searchUserUiState.update {
                        it.copy(
                            error = error.message,
                            isLoading = false
                        )
                    }
                }
            } else {
                _searchUserUiState.update {
                    it.copy(
                        error = "No podés asignarte a vos mismo como paciente",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onAssignAsPatient(user: AssignableUser) {
        viewModelScope.launch {
            _searchUserUiState.update {
                it.copy(
                    assigningPatient = true,
                    error = "",
                    patientAssignmentSuccess = false
                )
            }
            _userRepository.assignAsAPatient(user).onSuccess {
                _searchUserUiState.update {
                    it.copy(
                        userLoaded = false,
                        assigningPatient = false,
                        error = "",
                        patientAssignmentSuccess = true
                    )
                }
            }.onFailure { error ->
                _searchUserUiState.update {
                    it.copy(
                        userLoaded = false,
                        assigningPatient = false,
                        error = if (error.message.isNullOrEmpty()) "Error inesperado" else error.message,
                        patientAssignmentSuccess = false
                    )
                }
            }
        }
    }

    fun dismissSuccessMessage(updateState: (SearchUserUiState) -> SearchUserUiState) {
        _searchUserUiState.update(updateState)
    }

    fun dismissErrorMessage(updateState: (SearchUserUiState) -> SearchUserUiState) {
        _searchUserUiState.update(updateState)
    }
}