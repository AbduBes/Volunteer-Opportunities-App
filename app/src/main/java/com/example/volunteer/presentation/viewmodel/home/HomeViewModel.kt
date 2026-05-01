package com.example.volunteer.presentation.viewmodel.home

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
import com.example.volunteer.model.HardcodedData.allQuests as quests

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<HomeNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        loadActiveQuests()
    }

    private fun loadActiveQuests() {
        _uiState.value = HomeUiState.Success(
            activeQuests = quests.filter { it.isJoined }
        )
    }

    fun onQuestClicked(questId: Int) {
        viewModelScope.launch {
            _navigationEvent.send(HomeNavigationEvent.NavigateToQuestDetail(questId))
        }
    }
}