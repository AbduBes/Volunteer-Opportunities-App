package com.example.volunteer.presentation.viewmodel.auth

sealed interface AuthNavigationEvent {
    data object NavigateToHome : AuthNavigationEvent
    data class ShowMessage(val message: String) : AuthNavigationEvent
}
