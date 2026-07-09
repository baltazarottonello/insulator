package com.example.insulator.ui.viewModels.management.patients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insulator.core.data.repositories.patients.PatientRepository
import com.example.insulator.core.data.repositories.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchUserViewModel(
    private val _patientRepository: PatientRepository,
    private val _userRepository: UserRepository
) : ViewModel() {

    private val _searchUserUiState = MutableStateFlow(
        SearchUserUiState()
    )
    val searchUserUiState = _searchUserUiState as StateFlow<SearchUserUiState>

    fun onSearchUserByEmail(email: String) {
        viewModelScope.launch {
            _searchUserUiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    userLoaded = false,
                )
            }
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
        }
    }

    fun onAssignAsPatient(user: AssignableUser) {
        //TODO 10/07/26
        viewModelScope.launch {
            _searchUserUiState.update {
                it.copy(
                    assigningPatient = true,
                    error = "",
                    isSuccess = false
                )
            }
            _userRepository.assignAsAPatient(user).onSuccess {
                _searchUserUiState.update {
                    it.copy(
                        userLoaded = false,
                        assigningPatient = false,
                        error = "",
                        isSuccess = true
                    )
                }
            }.onFailure { error ->
                _searchUserUiState.update {
                    it.copy(
                        userLoaded = false,
                        assigningPatient = false,
                        error = if (error.message.isNullOrEmpty()) "Error inesperado" else error.message,
                        isSuccess = false
                    )
                }
            }
        }
    }
}