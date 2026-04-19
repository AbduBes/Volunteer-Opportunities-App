package com.example.volunteer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileUiState(
    val name: String = "Alex Johnson",
    val email: String = "alex@volunteer.org",
    val bio: String = "Passionate about helping the community.",
    val totalHours: Int = 24,
    val questsCompleted: Int = 7,
    val streak: Int = 5,
    val badges: List<String> = listOf("First Quest", "10 Hours", "Eco Warrior", "Team Player")
){
    // 1. Derived State: Calculate a level based on hours
    val userLevel: Int
        get() = (totalHours / 10) + 1

    // 2. Derived State: Format a welcome string or check milestone
    val isPowerVolunteer: Boolean
        get() = questsCompleted > 5
}


class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
}


