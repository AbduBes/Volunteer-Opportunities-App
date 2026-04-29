package com.example.volunteer.presentation.ui.screens.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.volunteer.presentation.ui.screens.add.components.ValidatedTextField
import com.example.volunteer.presentation.viewmodel.addQuest.AddQuestEvent
import com.example.volunteer.presentation.viewmodel.addQuest.AddQuestUiState
import com.example.volunteer.presentation.viewmodel.addQuest.AddQuestViewModel

// 1. Stateful Component
@Composable
fun AddQuestScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddQuestViewModel = hiltViewModel() // Use Hilt
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Listen to one-time events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AddQuestEvent.NavigateBack -> onBack()
                is AddQuestEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    // Only render the form if we are in the correct state
    if (uiState is AddQuestUiState.Form) {
        AddQuestScreenContent(
            state = uiState as AddQuestUiState.Form,
            snackbarHostState = snackbarHostState,
            viewModel = viewModel,
            modifier = modifier
        )
    }
}

// 2. Stateless Component
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddQuestScreenContent(
    state: AddQuestUiState.Form,
    snackbarHostState: SnackbarHostState,
    viewModel: AddQuestViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Add New Quest") },
                navigationIcon = {
                    IconButton(onClick = viewModel::onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ValidatedTextField(
                value = state.title,
                onValueChange = viewModel::onTitleChange,
                label = "Quest Title",
                errorMessage = state.titleError
            )

            ValidatedTextField(
                value = state.organization,
                onValueChange = viewModel::onOrganizationChange,
                label = "Organization",
                errorMessage = state.organizationError
            )

            ValidatedTextField(
                value = state.location,
                onValueChange = viewModel::onLocationChange,
                label = "Location",
                errorMessage = state.locationError
            )

            ValidatedTextField(
                value = state.hours,
                onValueChange = viewModel::onHoursChange,
                label = "Hours Required (1–100)",
                errorMessage = state.hoursError
            )

            ValidatedTextField(
                value = state.description,
                onValueChange = viewModel::onDescriptionChange,
                label = "Description (min. 20 chars)",
                errorMessage = state.descriptionError,
                singleLine = false
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = viewModel::onSubmit,
                enabled = state.isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Quest")
            }

            if (!state.isFormValid) {
                Text(
                    text = "Please fill in all fields correctly to enable submit.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}