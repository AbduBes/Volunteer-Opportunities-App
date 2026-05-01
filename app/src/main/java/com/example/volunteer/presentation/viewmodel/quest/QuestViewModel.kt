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

@HiltViewModel
class QuestsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<QuestsUiState>(QuestsUiState.Loading)
    val uiState: StateFlow<QuestsUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<QuestsNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

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
        val currentState = _uiState.value as? QuestsUiState.Success ?: return
        val filtered = if (category == "All") HardcodedData.allQuests
        else HardcodedData.allQuests.filter { it.category == category }
        _uiState.value = currentState.copy(selectedCategory = category, allQuests = filtered)
    }

    fun toggleFavorite(questId: Int) {
        val currentState = _uiState.value as? QuestsUiState.Success ?: return
        val updated = currentState.allQuests.map {
            if (it.id == questId) it.copy(isFavorite = !it.isFavorite) else it
        }
        _uiState.value = currentState.copy(allQuests = updated)
    }

    fun setGroupedLayout(isGrouped: Boolean) {
        val currentState = _uiState.value as? QuestsUiState.Success ?: return
        _uiState.value = currentState.copy(isGroupedLayout = isGrouped)
    }

    fun onQuestClicked(questId: Int) {
        viewModelScope.launch {
            _navigationEvent.send(QuestsNavigationEvent.NavigateToQuestDetail(questId))
        }
    }

    fun getQuestById(id: Int): Quest? = HardcodedData.allQuests.find { it.id == id }
}