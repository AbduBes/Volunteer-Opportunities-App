package com.example.volunteer.presentation.viewmodel.quest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volunteer.model.HardcodedData
import com.example.volunteer.model.Quest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. Represent UI state using a sealed class/interface
sealed interface QuestsUiState {
    data object Loading : QuestsUiState
    data class Success(
        val allQuests: List<Quest> = emptyList(),
        val categories: List<String> = emptyList(),
        val selectedCategory: String = "All",
        val isGroupedLayout: Boolean = false // Moved from Compose to ViewModel state
    ) : QuestsUiState
}

// 2. Handle one-time events like navigation
sealed interface QuestsEvent {
    data class NavigateToQuestDetail(val questId: Int) : QuestsEvent
    data object NavigateToList : QuestsEvent
    data object NavigateToGrid : QuestsEvent
}

// 3. Annotate with @HiltViewModel and inject constructor
@HiltViewModel
class QuestsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<QuestsUiState>(QuestsUiState.Loading)
    val uiState: StateFlow<QuestsUiState> = _uiState.asStateFlow()

    private val _events = Channel<QuestsEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _uiState.value = QuestsUiState.Success(
            allQuests = HardcodedData.allQuests,
            categories = HardcodedData.categories
        )
    }

    fun selectCategory(category: String) {
        val currentState = _uiState.value
        if (currentState is QuestsUiState.Success) {
            val filtered = if (category == "All") {
                HardcodedData.allQuests
            } else {
                HardcodedData.allQuests.filter { it.category == category }
            }
            _uiState.value = currentState.copy(
                selectedCategory = category,
                allQuests = filtered
            )
        }
    }

    fun toggleFavorite(questId: Int) {
        val currentState = _uiState.value
        if (currentState is QuestsUiState.Success) {
            val updated = currentState.allQuests.map {
                if (it.id == questId) it.copy(isFavorite = !it.isFavorite) else it
            }
            _uiState.value = currentState.copy(allQuests = updated)
        }
    }

    fun setGroupedLayout(isGrouped: Boolean) {
        val currentState = _uiState.value
        if (currentState is QuestsUiState.Success) {
            _uiState.value = currentState.copy(isGroupedLayout = isGrouped)

            // Fire layout-based navigation events
            viewModelScope.launch {
                if (isGrouped) _events.send(QuestsEvent.NavigateToGrid)
                else _events.send(QuestsEvent.NavigateToList)
            }
        }
    }

    fun onQuestClicked(questId: Int) {
        viewModelScope.launch {
            _events.send(QuestsEvent.NavigateToQuestDetail(questId))
        }
    }

    fun getQuestById(id: Int): Quest? = HardcodedData.allQuests.find { it.id == id }
}