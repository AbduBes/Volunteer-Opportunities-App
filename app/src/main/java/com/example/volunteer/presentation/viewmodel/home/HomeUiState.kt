package com.example.volunteer.presentation.viewmodel.home

import com.example.volunteer.model.Quest

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val userName: String = "Alex",
        val totalHours: Int = 24,
        val questsCompleted: Int = 7,
        val activeQuests: List<Quest> = emptyList()
    ) : HomeUiState
}