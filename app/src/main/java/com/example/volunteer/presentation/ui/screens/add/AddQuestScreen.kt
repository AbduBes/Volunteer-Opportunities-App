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
import kotlinx.coroutines.launch
import com.example.volunteer.presentation.ui.screens.add.components.ValidatedTextField
import com.example.volunteer.presentation.viewmodel.addQuest.AddQuestNavigationEvent
import com.example.volunteer.presentation.viewmodel.addQuest.AddQuestUiState
import com.example.volunteer.presentation.viewmodel.addQuest.AddQuestViewModel

@Composable
fun AddQuestScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddQuestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is AddQuestNavigationEvent.NavigateBack -> onBack()
                is AddQuestNavigationEvent.ShowMessage -> launch { snackbarHostState.showSnackbar(event.message) }
            }
        }
    }

    val state = uiState
    when (state) {
        is AddQuestUiState.Init,
        is AddQuestUiState.Loading,
        is AddQuestUiState.Success -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is AddQuestUiState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        is AddQuestUiState.Form -> {
            AddQuestScreenContent(
                state = state,
                snackbarHostState = snackbarHostState,
                onBackClicked = viewModel::onBackClicked,
                onTitleChange = viewModel::onTitleChange,
                onOrganizationChange = viewModel::onOrganizationChange,
                onLocationChange = viewModel::onLocationChange,
                onHoursChange = viewModel::onHoursChange,
                onDescriptionChange = viewModel::onDescriptionChange,
                onCategorySelected = viewModel::onCategorySelected,
                onSubmit = viewModel::onSubmit,
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddQuestScreenContent(
    state: AddQuestUiState.Form,
    snackbarHostState: SnackbarHostState,
    onBackClicked: () -> Unit,
    onTitleChange: (String) -> Unit,
    onOrganizationChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onHoursChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategorySelected: (Int) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Add New Quest") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
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
            ValidatedTextField(value = state.title, onValueChange = onTitleChange, label = "Quest Title", errorMessage = state.titleError)
            ValidatedTextField(value = state.organization, onValueChange = onOrganizationChange, label = "Organization", errorMessage = state.organizationError)
            ValidatedTextField(value = state.location, onValueChange = onLocationChange, label = "Location", errorMessage = state.locationError)
            ValidatedTextField(value = state.hours, onValueChange = onHoursChange, label = "Hours Required (1–100)", errorMessage = state.hoursError)
            ValidatedTextField(value = state.description, onValueChange = onDescriptionChange, label = "Description (min. 20 chars)", errorMessage = state.descriptionError, singleLine = false)

            // Category dropdown
            var categoryExpanded by remember { mutableStateOf(false) }
            val selectedCategoryName = state.categories
                .find { it.first == state.selectedCategoryId }?.second ?: "Select a category"
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedCategoryName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    isError = state.categoryError != null,
                    supportingText = {
                        if (state.categoryError != null) {
                            Text(state.categoryError, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    state.categories.forEach { (id, name) ->
                        DropdownMenuItem(
                            text = { Text(name) },
                            onClick = {
                                onCategorySelected(id)
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onSubmit, enabled = state.isFormValid, modifier = Modifier.fillMaxWidth()) {
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