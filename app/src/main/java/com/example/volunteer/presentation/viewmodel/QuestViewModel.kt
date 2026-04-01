package com.example.volunteer.presentation.viewmodel

import androidx.lifecycle.ViewModel
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

    private val allQuests = listOf(
        Quest(1, "Park Cleanup", "Green City", "Environment", "City Park", 3, "Help clean the local park and plant new flowers."),
        Quest(2, "Food Bank Helper", "Community Aid", "Food", "Downtown", 4, "Sort and distribute food donations to families in need.", isJoined = true),
        Quest(3, "Animal Shelter", "Paws & Care", "Animals", "West Side", 2, "Walk and care for shelter animals awaiting adoption.", isJoined = true),
        Quest(4, "Reading Buddy", "City Library", "Education", "Main Library", 2, "Read books with children aged 5–10 every Saturday."),
        Quest(5, "Senior Visit", "Elder Care", "Healthcare", "Sunrise Home", 3, "Visit and chat with elderly residents."),
        Quest(6, "Beach Cleanup", "Ocean Friends", "Environment", "South Beach", 5, "Remove plastic waste from the coastline."),
        Quest(7, "Coding Workshop", "Tech4Good", "Education", "Community Center", 4, "Teach basic coding to teenagers."),
    )

    val categories = listOf("All", "Environment", "Food", "Animals", "Education", "Healthcare")

    init {
        _uiState.value = _uiState.value.copy(allQuests = allQuests)
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