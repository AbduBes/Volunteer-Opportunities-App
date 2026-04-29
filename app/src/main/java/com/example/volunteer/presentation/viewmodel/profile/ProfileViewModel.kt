package com.example.volunteer.presentation.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. Represent UI state using a sealed interface
sealed interface ProfileUiState {
    data object Loading : ProfileUiState

    data class Success(
        val name: String = "Alex Johnson",
        val email: String = "alex@volunteer.org",
        val bio: String = "Passionate about helping the community.",
        val totalHours: Int = 24,
        val questsCompleted: Int = 7,
        val streak: Int = 5,
        val badges: List<String> = listOf("First Quest", "10 Hours", "Eco Warrior", "Team Player")
    ) : ProfileUiState {
        // Derived States are perfectly fine inside the Success state
        val userLevel: Int
            get() = (totalHours / 10) + 1

        val isPowerVolunteer: Boolean
            get() = questsCompleted > 5
    }
}

// 2. Handle one-time events
sealed interface ProfileEvent {
    data class ShowBadgeDetail(val badgeName: String) : ProfileEvent
}

// 3. Annotate with @HiltViewModel and inject constructor
@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _events = Channel<ProfileEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        // Simulate network fetch
        _uiState.value = ProfileUiState.Success()
    }

    fun onBadgeClicked(badgeName: String) {
        viewModelScope.launch {
            _events.send(ProfileEvent.ShowBadgeDetail(badgeName))
        }
    }
}