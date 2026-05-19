package com.example.volunteer.presentation.viewmodel.questDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volunteer.model.mapper.toQuest
import com.example.volunteer.domain.repository.QuestRepository
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
class QuestDetailViewModel @Inject constructor(
    private val questRepository: QuestRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuestDetailUiState>(QuestDetailUiState.Init)
    val uiState: StateFlow<QuestDetailUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<QuestDetailNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun loadQuest(questId: Int) {
        _uiState.value = QuestDetailUiState.Loading
        viewModelScope.launch {
            val userId = sessionManager.currentAccountId.value
            val questWithDetails = questRepository.getWithDetailsById(questId)
            _uiState.value = if (questWithDetails != null) {
                val joined = userId != null && questRepository.isJoinedByUser(userId, questId)
                val quest = questWithDetails.toQuest().copy(isJoined = joined)
                QuestDetailUiState.Success(
                    quest = quest,
                    canDelete = userId != null && quest.creatorId == userId
                )
            } else {
                QuestDetailUiState.Error("Quest not found")
            }
        }
    }

    fun toggleJoin() {
        val currentState = _uiState.value as? QuestDetailUiState.Success ?: return
        val userId = sessionManager.currentAccountId.value ?: return
        val quest = currentState.quest
        val newJoined = !quest.isJoined
        viewModelScope.launch {
            questRepository.toggleJoin(userId = userId, questId = quest.id, isJoined = newJoined)
            _uiState.value = currentState.copy(quest = quest.copy(isJoined = newJoined))
            val message = if (newJoined) "Joined Quest!" else "Left Quest."
            _navigationEvent.send(QuestDetailNavigationEvent.ShowMessage(message))
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _navigationEvent.send(QuestDetailNavigationEvent.NavigateBack)
        }
    }

    fun deleteQuest() {
        val currentState = _uiState.value as? QuestDetailUiState.Success ?: return
        viewModelScope.launch {
            questRepository.deleteById(currentState.quest.id)
            _navigationEvent.send(QuestDetailNavigationEvent.QuestDeleted)
        }
    }
}
