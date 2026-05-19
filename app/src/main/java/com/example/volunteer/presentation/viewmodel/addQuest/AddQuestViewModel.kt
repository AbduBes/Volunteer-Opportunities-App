package com.example.volunteer.presentation.viewmodel.addQuest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volunteer.model.data.local.entity.OrganizationEntity
import com.example.volunteer.model.data.local.entity.QuestEntity
import com.example.volunteer.domain.repository.CategoryRepository
import com.example.volunteer.domain.repository.OrganizationRepository
import com.example.volunteer.domain.repository.QuestRepository
import com.example.volunteer.model.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddQuestViewModel @Inject constructor(
    private val questRepository: QuestRepository,
    private val organizationRepository: OrganizationRepository,
    private val categoryRepository: CategoryRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddQuestUiState>(AddQuestUiState.Init)
    val uiState: StateFlow<AddQuestUiState> = _uiState.asStateFlow()

    private val _navigationEvent = Channel<AddQuestNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            _uiState.value = AddQuestUiState.Loading
            val categoryFlow = categoryRepository.getAll()
            // Wait for the first emission so the form is never shown with an empty dropdown
            val initialCategories = categoryFlow.first().map { it.categoryId to it.name }
            _uiState.value = AddQuestUiState.Form(categories = initialCategories)
            // Keep collecting so the list stays fresh if categories change
            categoryFlow.collect { entities ->
                updateFormState { it.copy(categories = entities.map { c -> c.categoryId to c.name }) }
            }
        }
    }

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

    fun onCategorySelected(categoryId: Int) {
        updateFormState { it.copy(selectedCategoryId = categoryId, categoryError = null) }
    }

    private fun updateFormState(update: (AddQuestUiState.Form) -> AddQuestUiState.Form) {
        val currentState = _uiState.value as? AddQuestUiState.Form ?: return
        _uiState.value = update(currentState)
    }

    fun onSubmit() {
        val currentState = _uiState.value as? AddQuestUiState.Form ?: return
        if (!currentState.isFormValid) return
        _uiState.value = AddQuestUiState.Loading
        viewModelScope.launch {
            try {
                val orgName = currentState.organization.trim()
                val existingOrg = organizationRepository.getByName(orgName)
                val orgId = existingOrg?.organizationId ?: run {
                    val nextId = organizationRepository.nextId()
                    organizationRepository.insert(OrganizationEntity(nextId, orgName))
                    nextId
                }
                val questId = questRepository.nextId()
                questRepository.insert(
                    QuestEntity(
                        questId = questId,
                        title = currentState.title.trim(),
                        organizationId = orgId,
                        categoryId = currentState.selectedCategoryId,
                        location = currentState.location.trim(),
                        hoursRequired = currentState.hours.toInt(),
                        description = currentState.description.trim(),
                        creatorId = sessionManager.currentAccountId.value
                    )
                )
                _uiState.value = AddQuestUiState.Success
                _navigationEvent.send(AddQuestNavigationEvent.ShowMessage("Quest Added Successfully!"))
                _navigationEvent.send(AddQuestNavigationEvent.NavigateBack)
            } catch (e: Exception) {
                _uiState.value = AddQuestUiState.Error(e.message ?: "Failed to add quest")
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navigationEvent.send(AddQuestNavigationEvent.NavigateBack)
        }
    }
}
