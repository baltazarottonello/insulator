package com.nutricionbn.insulator.core.data.repositories.user

import com.nutricionbn.insulator.ui.viewModels.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionRepository {
    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    fun updateState(newUser: UserState) {
        _userState.value = newUser
    }
}