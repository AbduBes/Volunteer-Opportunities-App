package com.example.volunteer.presentation.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _events = Channel<ProfileEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        _uiState.value = ProfileUiState.Success()
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
}