package com.example.volunteer.presentation.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volunteer.model.Quest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.volunteer.model.HardcodedData.allQuests as quests

sealed interface HomeUiState {
    data object Loading: HomeUiState
    data class Success(
        val userName: String = "Alex",
        val totalHours: Int = 24,
        val questsCompleted: Int = 7,
        val activeQuests: List<Quest> = emptyList()
    ): HomeUiState
}

sealed interface HomeEvent {
    data class NavigateToQuestDetail(val questId: Int) : HomeEvent
}
@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    init{
        loadActiveQuests()
    }

    private fun loadActiveQuests() {
        _uiState.value = HomeUiState.Success(
            activeQuests = quests.filter { it.isJoined }
        )
    }
    fun onQuestClicked(questId: Int) {
        viewModelScope.launch {
            _events.send(HomeEvent.NavigateToQuestDetail(questId))
        }
    }
}
