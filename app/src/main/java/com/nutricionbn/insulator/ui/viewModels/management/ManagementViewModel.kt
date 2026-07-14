package com.nutricionbn.insulator.ui.viewModels.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutricionbn.insulator.core.data.internal.constants.Roles
import com.nutricionbn.insulator.core.data.repositories.user.SessionRepository
import com.nutricionbn.insulator.ui.viewModels.UserState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ManagementViewModel(private val _sessionRepository: SessionRepository) : ViewModel() {

    val userState: StateFlow<UserState> = _sessionRepository.userState;

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