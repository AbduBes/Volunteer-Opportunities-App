package com.example.volunteer.presentation.viewmodel.questDetail

sealed interface QuestDetailNavigationEvent {
    data object NavigateBack : QuestDetailNavigationEvent
    data class ShowMessage(val message: String) : QuestDetailNavigationEvent
}