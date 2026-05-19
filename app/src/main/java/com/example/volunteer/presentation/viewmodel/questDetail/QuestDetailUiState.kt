package com.example.volunteer.presentation.viewmodel.questDetail

import com.example.volunteer.model.Quest

sealed interface QuestDetailUiState {
    data object Init : QuestDetailUiState
    data object Loading : QuestDetailUiState
    data class Success(
        val quest: Quest,
        val canDelete: Boolean = false
    ) : QuestDetailUiState
    data class Error(val message: String) : QuestDetailUiState
}