package com.example.insulator.core.data.repositories

data class UserState(
    val id : String = "",
    val email : String = "",
    val avatar : String = "",
    val fullName : String = "",
    val accessToken: String = "",
    val refreshToken: String = "",
    val isLogged: Boolean = false
)

