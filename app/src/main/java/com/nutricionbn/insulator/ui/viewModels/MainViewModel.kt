package com.nutricionbn.insulator.ui.viewModels

import androidx.lifecycle.ViewModel
import com.nutricionbn.insulator.core.data.repositories.user.SessionRepository
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val _sessionRepository : SessionRepository) : ViewModel() {

    val userState: StateFlow<UserState> = _sessionRepository.userState;

}