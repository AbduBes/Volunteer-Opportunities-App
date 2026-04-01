package com.example.volunteer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.volunteer.model.Quest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val userName: String = "Alex",
    val totalHours: Int = 24,
    val questsCompleted: Int = 7,
    val activeQuests: List<Quest> = emptyList()
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadActiveQuests()
    }

    private fun loadActiveQuests() {
        val quests = listOf(
            Quest(1, "Park Cleanup", "Green City", "Environment", "City Park", 3, "Help clean the local park."),
            Quest(2, "Food Bank Helper", "Community Aid", "Food", "Downtown", 4, "Sort and distribute food donations.", isJoined = true),
            Quest(3, "Animal Shelter", "Paws & Care", "Animals", "West Side", 2, "Walk and care for shelter animals.", isJoined = true)
        )
        _uiState.value = _uiState.value.copy(activeQuests = quests.filter { it.isJoined })
    }
}