package com.example.insulator.core.data.ui

data class AuthenticationUiState(
    val isLoading: Boolean = false,
    val error : String? = "",
    val isSuccess: Boolean = false,
)
