package com.example.volunteer.presentation.viewmodel.questDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volunteer.model.HardcodedData.allQuests
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<QuestDetailUiState>(QuestDetailUiState.Loading)
    val uiState: StateFlow<QuestDetailUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<QuestDetailNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun loadQuest(questId: Int) {
        viewModelScope.launch {
            val quest = allQuests.find { it.id == questId }
            _uiState.value = if (quest != null) QuestDetailUiState.Success(quest)
            else QuestDetailUiState.Error("Quest not found")
        }
    }

    fun toggleJoin() {
        val currentState = _uiState.value as? QuestDetailUiState.Success ?: return
        val updatedQuest = currentState.quest.copy(isJoined = !currentState.quest.isJoined)
        _uiState.value = QuestDetailUiState.Success(updatedQuest)
        viewModelScope.launch {
            val message = if (updatedQuest.isJoined) "Joined Quest!" else "Left Quest."
            _navigationEvent.send(QuestDetailNavigationEvent.ShowMessage(message))
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _navigationEvent.send(QuestDetailNavigationEvent.NavigateBack)
        }
    }
}