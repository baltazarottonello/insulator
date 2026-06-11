package com.example.insulator.core.viewModels

import androidx.lifecycle.ViewModel
import com.example.insulator.core.data.repositories.IUserRepository
import com.example.insulator.core.data.repositories.UserState
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val userRepository: IUserRepository) : ViewModel() {

    val userState: StateFlow<UserState> = userRepository.userState;

}