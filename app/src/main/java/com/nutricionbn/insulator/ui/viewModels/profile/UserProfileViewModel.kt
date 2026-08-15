package com.nutricionbn.insulator.ui.viewModels.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutricionbn.insulator.core.data.dto.user.DeleteUserDataRequest
import com.nutricionbn.insulator.core.data.dto.user.UpdateUserProfileDataRequest
import com.nutricionbn.insulator.core.data.repositories.tokens.TokenRepository
import com.nutricionbn.insulator.core.data.repositories.user.SessionRepository
import com.nutricionbn.insulator.core.data.repositories.user.UserRepository
import com.nutricionbn.insulator.ui.viewModels.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val _tokenRepository: TokenRepository,
    private val _sessionRepository: SessionRepository,
    private val _userRepository: UserRepository,
) : ViewModel() {

    val userState: StateFlow<UserState> = _sessionRepository.userState;
    private val _uiState = MutableStateFlow(
        UserProfileUiState(
            name = userState.value.firstName,
            lastName = userState.value.lastName,
            email = userState.value.email
        )
    )
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()
    fun onFirstNameChanged(newName: String) {
        if (userState.value.firstName != newName) {
            _uiState.update { it.copy(isDataChanged = true) }
        } else {
            _uiState.update { it.copy(isDataChanged = false) }
        }
        _uiState.update { it.copy(name = newName, error = "") }
    }

    fun onLastNameChanged(newLastName: String) {
        if (userState.value.lastName != newLastName) {
            _uiState.update { it.copy(isDataChanged = true) }
        } else {
            _uiState.update { it.copy(isDataChanged = false) }
        }
        _uiState.update { it.copy(lastName = newLastName, error = "") }
    }

    fun onSaveClick() {
        _uiState.update { it.copy(showSaveChangesConfirmationModal = true) }
    }

    fun onSaveConfirmed() {
        viewModelScope.launch {
            _uiState.update { it.copy(modalLoading = true, error = "") }
            _userRepository.updateProfileData(
                UpdateUserProfileDataRequest(
                    firstName = _uiState.value.name,
                    lastName = _uiState.value.lastName
                )
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        modalLoading = false,
                        error = "",
                        showSaveChangesConfirmationModal = false,
                        showSaveChangesSuccessModal = true
                    )
                }
                _sessionRepository.updateState(
                    newUser = userState.value.copy(
                        firstName = _uiState.value.name,
                        lastName = _uiState.value.lastName
                    )
                )
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        showSaveChangesConfirmationModal = false,
                        modalLoading = false,
                        error = error.message!!,
                    )
                }
            }
        }
    }

    fun onSaveDismiss() {
        _uiState.update { it.copy(showSaveChangesConfirmationModal = false) }
    }

    fun onDismissSaveChangesSuccessModal() {
        _uiState.update { it.copy(showSaveChangesSuccessModal = false) }
    }

    fun onDeleteAccountClick() {
        _uiState.update { it.copy(showDeleteAccountConfirmationModal = true) }
    }

    fun onDeleteAccountConfirmed() {
        viewModelScope.launch {
            _uiState.update { it.copy(modalLoading = true, error = "") }
            _userRepository.deleteUserData()
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            showDeleteAccountConfirmationModal = false,
                            modalLoading = false,
                            accountDeletionSuccess = true
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            showDeleteAccountConfirmationModal = false,
                            modalLoading = false,
                            error = error.message!!
                        )
                    }
                }
        }
    }

    fun onDeleteAccountDismiss() {
        _uiState.update { it.copy(showDeleteAccountConfirmationModal = false) }
    }

    fun onSignOutClick() {
        _uiState.update { it.copy(showSignOutConfirmationModal = true) }
    }

    suspend fun onSignOutConfirmed() {
        _tokenRepository.clearTokens()
        _sessionRepository.updateState(UserState())
    }

    fun onSignOutDismiss() {
        _uiState.update { it.copy(showSignOutConfirmationModal = false) }
    }
}