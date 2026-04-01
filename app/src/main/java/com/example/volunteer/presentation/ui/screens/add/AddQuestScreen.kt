package com.example.volunteer.presentation.ui.screens.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.volunteer.presentation.ui.screens.add.components.ValidatedTextField
import com.example.volunteer.presentation.viewmodel.AddQuestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuestScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddQuestViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isSubmitted) {
        if (state.isSubmitted) {
            onBack()
            viewModel.resetForm()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Quest") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Create a Volunteer Quest",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

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