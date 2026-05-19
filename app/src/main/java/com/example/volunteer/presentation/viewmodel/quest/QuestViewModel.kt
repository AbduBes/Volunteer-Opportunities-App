package com.example.volunteer.presentation.viewmodel.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volunteer.model.Quest
import com.example.volunteer.model.data.mapper.toQuest
import com.example.volunteer.model.data.repository.CategoryRepository
import com.example.volunteer.model.data.repository.QuestRepository
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
class QuestsViewModel @Inject constructor(
    private val questRepository: QuestRepository,
    private val categoryRepository: CategoryRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuestsUiState>(QuestsUiState.Init)
    val uiState: StateFlow<QuestsUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<QuestsNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val _selectedCategory = MutableStateFlow("All")
    private val _isGroupedLayout = MutableStateFlow(false)

    init {
        _uiState.value = QuestsUiState.Loading
        val userId = sessionManager.currentAccountId.value
        if (userId == null) {
            _uiState.value = QuestsUiState.Error("Not signed in")
        } else {
            observeQuests(userId)
        }
    }

    private fun observeQuests(userId: Int) {
        viewModelScope.launch {
            combine(
                questRepository.getAvailableWithDetails(userId),
                categoryRepository.getAll(),
                _selectedCategory,
                _isGroupedLayout
            ) { quests, categories, selectedCategory, isGrouped ->
                val allQuests = quests.map { it.toQuest() }
                val filtered = if (selectedCategory == "All") allQuests
                               else allQuests.filter { it.category == selectedCategory }
                QuestsUiState.Success(
                    allQuests = filtered,
                    categories = listOf("All") + categories.map { it.name },
                    selectedCategory = selectedCategory,
                    isGroupedLayout = isGrouped
                )
            }.collect { _uiState.value = it }
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(questId: Int) {
        val currentState = _uiState.value as? QuestsUiState.Success ?: return
        val quest = currentState.allQuests.find { it.id == questId } ?: return
        viewModelScope.launch {
            questRepository.updateFavoriteStatus(questId, !quest.isFavorite)
        }
    }

    fun setGroupedLayout(isGrouped: Boolean) {
        _isGroupedLayout.value = isGrouped
    }

    fun onQuestClicked(questId: Int) {
        viewModelScope.launch {
            _navigationEvent.send(QuestsNavigationEvent.NavigateToQuestDetail(questId))
        }
    }

    fun getQuestById(id: Int): Quest? =
        (_uiState.value as? QuestsUiState.Success)?.allQuests?.find { it.id == id }
}
