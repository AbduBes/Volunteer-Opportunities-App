package com.example.volunteer.presentation.viewmodel.profile

sealed interface ProfileEvent {
    data class ShowBadgeDetail(val badgeName: String) : ProfileEvent
    data object Navigate : ProfileEvent
    data object NavigateBack : ProfileEvent
}