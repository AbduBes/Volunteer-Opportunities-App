package com.example.volunteer.presentation.viewmodel.addQuest

sealed interface AddQuestUiState {
    data object Init : AddQuestUiState
    data object Loading : AddQuestUiState
    data object Success : AddQuestUiState
    data class Error(val message: String) : AddQuestUiState
    data class Form(
        val title: String = "",
        val organization: String = "",
        val location: String = "",
        val hours: String = "",
        val description: String = "",
        val categories: List<Pair<Int, String>> = emptyList(),
        val selectedCategoryId: Int = 0,
        val titleError: String? = null,
        val organizationError: String? = null,
        val locationError: String? = null,
        val hoursError: String? = null,
        val descriptionError: String? = null,
        val categoryError: String? = null
    ) : AddQuestUiState {
        val isFormValid: Boolean
            get() = title.isNotBlank()
                    && organization.isNotBlank()
                    && location.isNotBlank()
                    && hours.isNotBlank()
                    && description.length >= 20
                    && hours.toIntOrNull() != null
                    && (hours.toIntOrNull() ?: 0) in 1..100
                    && selectedCategoryId > 0
    }
}