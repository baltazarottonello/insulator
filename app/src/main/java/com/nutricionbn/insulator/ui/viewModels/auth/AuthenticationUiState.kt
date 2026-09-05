package com.nutricionbn.insulator.ui.viewModels.auth

data class AuthenticationUiState(
    val isLoading: Boolean = false,
    val error : String = "",
    val isSuccess: Boolean = false,
    val interactionDisabled : Boolean = false,
)