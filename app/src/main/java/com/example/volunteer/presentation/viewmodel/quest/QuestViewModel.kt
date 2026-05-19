package com.example.volunteer.presentation.viewmodel.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volunteer.domain.data.Quest
import com.example.volunteer.model.data.network.mapper.toQuest
import com.example.volunteer.model.repository.quest.QuestApiRepository
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
    private val questApiRepository: QuestApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuestsUiState>(QuestsUiState.Init)
    val uiState: StateFlow<QuestsUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<QuestsNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    // null = not yet loaded from the network (keeps the Loading state visible).
    private val _allQuests = MutableStateFlow<List<Quest>?>(null)
    private val _selectedCategory = MutableStateFlow("All")
    private val _isGroupedLayout = MutableStateFlow(false)

    init {
        observeQuests()
        loadQuests()
    }

    private fun observeQuests() {
        viewModelScope.launch {
            combine(
                _allQuests,
                _selectedCategory,
                _isGroupedLayout
            ) { quests, selectedCategory, isGrouped ->
                if (quests == null) {
                    QuestsUiState.Loading
                } else {
                    val filtered = if (selectedCategory == "All") quests
                                   else quests.filter { it.category == selectedCategory }
                    QuestsUiState.Success(
                        allQuests = filtered,
                        categories = listOf("All") + quests.map { it.category }.distinct(),
                        selectedCategory = selectedCategory,
                        isGroupedLayout = isGrouped
                    )
                }
            }.collect { state ->
                // Don't let a stale Loading emission overwrite a terminal Error.
                if (_uiState.value !is QuestsUiState.Error) {
                    _uiState.value = state
                }
            }
        }
    }

    private fun loadQuests() {
        viewModelScope.launch {
            try {
                _allQuests.value = questApiRepository.getQuests().map { it.toQuest() }
            } catch (e: Exception) {
                _uiState.value = QuestsUiState.Error(e.message ?: "Failed to load quests")
            }
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(questId: Int) {
        _allQuests.value = _allQuests.value?.map { quest ->
            if (quest.id == questId) quest.copy(isFavorite = !quest.isFavorite) else quest
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
