package com.example.volunteer.presentation.viewmodel.quest

import com.example.volunteer.model.Quest

sealed interface QuestsUiState {
    data object Init : QuestsUiState
    data object Loading : QuestsUiState
    data class Success(
        val allQuests: List<Quest> = emptyList(),
        val categories: List<String> = emptyList(),
        val selectedCategory: String = "All",
        val isGroupedLayout: Boolean = false
    ) : QuestsUiState
    data class Error(val message: String) : QuestsUiState
}