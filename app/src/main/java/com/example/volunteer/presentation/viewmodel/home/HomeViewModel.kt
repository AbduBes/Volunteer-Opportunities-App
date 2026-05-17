package com.example.volunteer.presentation.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volunteer.model.data.mapper.toQuest
import com.example.volunteer.model.data.repository.QuestRepository
import com.example.volunteer.model.data.repository.UserProfileRepository
import com.example.volunteer.model.data.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val questRepository: QuestRepository,
    private val userProfileRepository: UserProfileRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Init)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<HomeNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        _uiState.value = HomeUiState.Loading
        val userId = sessionManager.currentAccountId.value
        if (userId == null) {
            _uiState.value = HomeUiState.Error("Not signed in")
        } else {
            viewModelScope.launch {
                combine(
                    questRepository.getJoinedWithDetailsForUser(userId),
                    questRepository.getCreatedWithDetails(userId),
                    userProfileRepository.getById(userId)
                ) { joined, created, profile ->
                    HomeUiState.Success(
                        userName = profile?.name ?: "Volunteer",
                        totalHours = profile?.totalHours ?: 0,
                        questsCompleted = profile?.questsCompleted ?: 0,
                        activeQuests = joined.map { it.toQuest() },
                        createdQuests = created.map { it.toQuest() }
                    )
                }.collect { _uiState.value = it }
            }
        }
    }

    fun onQuestClicked(questId: Int) {
        viewModelScope.launch {
            _navigationEvent.send(HomeNavigationEvent.NavigateToQuestDetail(questId))
        }
    }

    fun deleteQuest(questId: Int) {
        viewModelScope.launch {
            questRepository.deleteById(questId)
        }
    }
}
