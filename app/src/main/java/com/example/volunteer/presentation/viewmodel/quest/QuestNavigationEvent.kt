package com.example.volunteer.presentation.viewmodel.quest

sealed interface QuestsNavigationEvent {
    data class NavigateToQuestDetail(val questId: Int) : QuestsNavigationEvent
}