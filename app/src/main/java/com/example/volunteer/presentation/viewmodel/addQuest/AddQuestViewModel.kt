package com.example.volunteer.presentation.viewmodel.addQuest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddQuestViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<AddQuestUiState>(AddQuestUiState.Form())
    val uiState: StateFlow<AddQuestUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<AddQuestNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onTitleChange(value: String) {
        val error = if (value.isBlank()) "Title cannot be empty" else null
        updateFormState { it.copy(title = value, titleError = error) }
    }

    fun onOrganizationChange(value: String) {
        val error = if (value.isBlank()) "Organization cannot be empty" else null
        updateFormState { it.copy(organization = value, organizationError = error) }
    }

    fun onLocationChange(value: String) {
        val error = if (value.isBlank()) "Location cannot be empty" else null
        updateFormState { it.copy(location = value, locationError = error) }
    }

    fun onHoursChange(value: String) {
        val num = value.toIntOrNull()
        val error = when {
            value.isBlank() -> "Hours cannot be empty"
            num == null -> "Must be a number"
            num !in 1..100 -> "Must be between 1 and 100"
            else -> null
        }
        updateFormState { it.copy(hours = value, hoursError = error) }
    }

    fun onDescriptionChange(value: String) {
        val error = if (value.length < 20) "Description must be at least 20 characters (${value.length}/20)" else null
        updateFormState { it.copy(description = value, descriptionError = error) }
    }

    private fun updateFormState(update: (AddQuestUiState.Form) -> AddQuestUiState.Form) {
        val currentState = _uiState.value as? AddQuestUiState.Form ?: return
        _uiState.value = update(currentState)
    }

    fun onSubmit() {
        val currentState = _uiState.value as? AddQuestUiState.Form ?: return
        if (currentState.isFormValid) {
            viewModelScope.launch {
                _navigationEvent.send(AddQuestNavigationEvent.ShowMessage("Quest Added Successfully!"))
                _navigationEvent.send(AddQuestNavigationEvent.NavigateBack)
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.send(AddQuestNavigationEvent.NavigateBack)
        }
    }
}