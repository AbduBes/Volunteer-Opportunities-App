package com.example.volunteer.presentation.viewmodel.addQuest

sealed interface AddQuestNavigationEvent {
    data object NavigateBack : AddQuestNavigationEvent
    data class ShowMessage(val message: String) : AddQuestNavigationEvent
}