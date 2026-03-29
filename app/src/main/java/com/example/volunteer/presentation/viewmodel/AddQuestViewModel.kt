package com.example.volunteer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AddQuestUiState(
    val title: String = "",
    val organization: String = "",
    val location: String = "",
    val hours: String = "",
    val description: String = "",
    val titleError: String? = null,
    val organizationError: String? = null,
    val locationError: String? = null,
    val hoursError: String? = null,
    val descriptionError: String? = null,
    val isSubmitted: Boolean = false
) {
    val isFormValid: Boolean
        get() = title.isNotBlank()
                && organization.isNotBlank()
                && location.isNotBlank()
                && hours.isNotBlank()
                && description.length >= 20
                && hours.toIntOrNull() != null
                && (hours.toIntOrNull() ?: 0) in 1..100
}

class AddQuestViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddQuestUiState())
    val uiState: StateFlow<AddQuestUiState> = _uiState.asStateFlow()

    fun onTitleChange(value: String) {
        val error = if (value.isBlank()) "Title cannot be empty" else null
        _uiState.value = _uiState.value.copy(title = value, titleError = error)
    }

    fun onOrganizationChange(value: String) {
        val error = if (value.isBlank()) "Organization cannot be empty" else null
        _uiState.value = _uiState.value.copy(organization = value, organizationError = error)
    }

    fun onLocationChange(value: String) {
        val error = if (value.isBlank()) "Location cannot be empty" else null
        _uiState.value = _uiState.value.copy(location = value, locationError = error)
    }

    fun onHoursChange(value: String) {
        val num = value.toIntOrNull()
        val error = when {
            value.isBlank() -> "Hours cannot be empty"
            num == null -> "Must be a number"
            num !in 1..100 -> "Must be between 1 and 100"
            else -> null
        }
        _uiState.value = _uiState.value.copy(hours = value, hoursError = error)
    }

    fun onDescriptionChange(value: String) {
        val error = if (value.length < 20) "Description must be at least 20 characters (${value.length}/20)" else null
        _uiState.value = _uiState.value.copy(description = value, descriptionError = error)
    }

    fun onSubmit() {
        if (_uiState.value.isFormValid) {
            _uiState.value = _uiState.value.copy(isSubmitted = true)
        }
    }

    fun resetForm() {
        _uiState.value = AddQuestUiState()
    }
}