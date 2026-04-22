package com.example.volunteer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.volunteer.model.HardcodedData
import com.example.volunteer.model.HardcodedData.allQuests
import com.example.volunteer.model.Quest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class QuestsUiState(
    val allQuests: List<Quest> = emptyList(),
    val selectedCategory: String = "All",
    val isLoading: Boolean = false
)

class QuestsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(QuestsUiState())
    val uiState: StateFlow<QuestsUiState> = _uiState.asStateFlow()
    val categories = HardcodedData.categories

    init {
        _uiState.value = _uiState.value.copy(allQuests = HardcodedData.allQuests)
    }

    fun selectCategory(category: String) {
        val filtered = if (category == "All") allQuests else allQuests.filter { it.category == category }
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            allQuests = filtered
        )
    }

    fun toggleFavorite(questId: Int) {
        val updated = _uiState.value.allQuests.map {
            if (it.id == questId) it.copy(isFavorite = !it.isFavorite) else it
        }
        _uiState.value = _uiState.value.copy(allQuests = updated)
    }

    fun getQuestById(id: Int): Quest? = allQuests.find { it.id == id }
}