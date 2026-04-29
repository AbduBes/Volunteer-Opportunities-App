package com.example.volunteer.presentation.viewmodel.questDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volunteer.model.HardcodedData.allQuests
import com.example.volunteer.model.Quest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. Sealed UI States
sealed interface QuestDetailUiState {
    data object Loading : QuestDetailUiState
    data class Success(val quest: Quest) : QuestDetailUiState
    data class Error(val message: String) : QuestDetailUiState
}

// 2. Sealed One-Time Events
sealed interface QuestDetailEvent {
    data object NavigateBack : QuestDetailEvent
    data class ShowMessage(val message: String) : QuestDetailEvent
}

@HiltViewModel
class QuestDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<QuestDetailUiState>(QuestDetailUiState.Loading)
    val uiState: StateFlow<QuestDetailUiState> = _uiState.asStateFlow()

    private val _events = Channel<QuestDetailEvent>()
    val events = _events.receiveAsFlow()

    // Triggered by the Compose Screen when it opens
    fun loadQuest(questId: Int) {
        viewModelScope.launch {
            val quest = allQuests.find { it.id == questId }
            if (quest != null) {
                _uiState.value = QuestDetailUiState.Success(quest)
            } else {
                _uiState.value = QuestDetailUiState.Error("Quest not found")
            }
        }
    }

    fun toggleJoin() {
        val currentState = _uiState.value
        if (currentState is QuestDetailUiState.Success) {
            val updatedQuest = currentState.quest.copy(isJoined = !currentState.quest.isJoined)
            _uiState.value = QuestDetailUiState.Success(updatedQuest)

            viewModelScope.launch {
                val message = if (updatedQuest.isJoined) "Joined Quest!" else "Left Quest."
                // Fixed typo here: changed QuestDetailScreen to QuestDetailEvent
                _events.send(QuestDetailEvent.ShowMessage(message))
            }
        }
    }

    // Handles the intent to go back centrally
    fun navigateBack() {
        viewModelScope.launch {
            _events.send(QuestDetailEvent.NavigateBack)
        }
    }
}