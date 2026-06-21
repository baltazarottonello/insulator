package com.example.insulator.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.insulator.core.data.repositories.user.IUserRepository
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val userRepository: IUserRepository) : ViewModel() {

    val userState: StateFlow<UserState> = userRepository.userState;

}