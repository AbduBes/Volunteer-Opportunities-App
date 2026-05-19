package com.example.volunteer.presentation.viewmodel.home

sealed interface HomeNavigationEvent {
    data class NavigateToQuestDetail(val questId: Int) : HomeNavigationEvent
}