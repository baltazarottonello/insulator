package com.nutricionbn.insulator.ui.viewModels.profile

data class UserProfileUiState(
    val error: String = "",
    val modalLoading: Boolean = false,

    //Data editing flow
    val showSaveChangesConfirmationModal: Boolean = false,
    val showSaveChangesSuccessModal: Boolean = false,

    //Delete account flow
    val showDeleteAccountConfirmationModal: Boolean = false,
    val accountDeletionSuccess: Boolean = false,

    // Sign out flow
    val showSignOutConfirmationModal: Boolean = false,

    //Form data
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val isDataChanged: Boolean = false,
)