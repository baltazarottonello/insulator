package com.example.insulator.ui.viewModels.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insulator.core.data.internal.constants.Roles
import com.example.insulator.core.data.repositories.user.IUserRepository
import com.example.insulator.ui.viewModels.UserState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ManagementViewModel(private val userRepository: IUserRepository) : ViewModel() {

    val userState: StateFlow<UserState> = userRepository.userState;

    private val _uiState: StateFlow<ManagementUiState> = userState
        .map { user ->
            ManagementUiState(isMedic = user.roles.contains(Roles.MEDIC))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ManagementUiState(isMedic = false)
        )
    val uiState: StateFlow<ManagementUiState> = _uiState
}