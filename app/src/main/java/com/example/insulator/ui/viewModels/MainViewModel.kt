package com.example.insulator.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.insulator.core.data.repositories.user.IUserRepository
import com.example.insulator.core.data.repositories.user.SessionManager
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val _sessionManager : SessionManager) : ViewModel() {

    val userState: StateFlow<UserState> = _sessionManager.userState;

}