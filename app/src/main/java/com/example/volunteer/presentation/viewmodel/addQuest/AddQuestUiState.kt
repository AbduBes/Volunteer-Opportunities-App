package com.example.volunteer.presentation.viewmodel.addQuest

sealed interface AddQuestUiState {
    data class Form(
        val title: String = "",
        val organization: String = "",
        val location: String = "",
        val hours: String = "",
        val description: String = "",
        val titleError: String? = null,
        val organizationError: String? = null,
        val locationError: String? = null,
        val hoursError: String? = null,
        val descriptionError: String? = null
    ) : AddQuestUiState {
        val isFormValid: Boolean
            get() = title.isNotBlank()
                    && organization.isNotBlank()
                    && location.isNotBlank()
                    && hours.isNotBlank()
                    && description.length >= 20
                    && hours.toIntOrNull() != null
                    && (hours.toIntOrNull() ?: 0) in 1..100
    }
}