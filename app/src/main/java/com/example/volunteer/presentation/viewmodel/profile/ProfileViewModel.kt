package com.example.volunteer.presentation.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volunteer.domain.repository.UserProfileRepository
import com.example.volunteer.model.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Init)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _events = Channel<ProfileEvent>()
    val events = _events.receiveAsFlow()

    init {
        _uiState.value = ProfileUiState.Loading
        val accountId = sessionManager.currentAccountId.value
        if (accountId == null) {
            _uiState.value = ProfileUiState.Error("Not signed in")
        } else {
            loadProfile(accountId)
        }
    }

    private fun loadProfile(accountId: Int) {
        viewModelScope.launch {
            userProfileRepository.getById(accountId).collect { profile ->
                _uiState.value = if (profile != null) {
                    ProfileUiState.Success(
                        name = profile.name,
                        email = profile.email,
                        bio = profile.bio,
                        totalHours = profile.totalHours,
                        questsCompleted = profile.questsCompleted,
                        streak = profile.streak
                    )
                } else {
                    ProfileUiState.Error("Profile not found")
                }
            }
        }
    }

    fun onBadgeClicked(badgeName: String) {
        viewModelScope.launch {
            _events.send(ProfileEvent.ShowBadgeDetail(badgeName))
        }
    }

    fun onNavigate() {
        viewModelScope.launch {
            _events.send(ProfileEvent.Navigate)
        }
    }

    fun onNavigateBack() {
        viewModelScope.launch {
            _events.send(ProfileEvent.NavigateBack)
        }
    }

    fun logout() {
        sessionManager.clear()
        viewModelScope.launch {
            _events.send(ProfileEvent.LoggedOut)
        }
    }
}
