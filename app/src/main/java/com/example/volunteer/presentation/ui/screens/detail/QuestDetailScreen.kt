package com.example.volunteer.presentation.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.volunteer.presentation.ui.screens.detail.components.DetailInfoRow
import com.example.volunteer.presentation.viewmodel.questDetail.QuestDetailNavigationEvent
import com.example.volunteer.presentation.viewmodel.questDetail.QuestDetailUiState
import com.example.volunteer.presentation.viewmodel.questDetail.QuestDetailViewModel

@Composable
fun QuestDetailScreen(
    questId: Int,
    source: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuestDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(questId) {
        viewModel.loadQuest(questId)
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is QuestDetailNavigationEvent.NavigateBack -> onBack()
                is QuestDetailNavigationEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
                is QuestDetailNavigationEvent.QuestDeleted -> onBack()
            }
        }
    }

    QuestDetailScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = viewModel::navigateBack,
        onJoinToggle = viewModel::toggleJoin,
        onDeleteClick = viewModel::deleteQuest,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestDetailScreenContent(
    uiState: QuestDetailUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onJoinToggle: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Quest") },
            text = { Text("Are you sure you want to delete this quest? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(if (uiState is QuestDetailUiState.Success) uiState.quest.title else "Quest Detail") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState is QuestDetailUiState.Success && uiState.canDelete) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Quest")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (uiState) {
                is QuestDetailUiState.Init,
                is QuestDetailUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is QuestDetailUiState.Error -> Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
                is QuestDetailUiState.Success -> {
                    val quest = uiState.quest
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(text = quest.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Text(text = quest.organization, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                        DetailInfoRow(label = "Category", value = quest.category)
                        DetailInfoRow(label = "Location", value = quest.location)
                        DetailInfoRow(label = "Hours Required", value = "${quest.hoursRequired} hours")
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "About this Quest", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = quest.description, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        Button(onClick = onJoinToggle, modifier = Modifier.fillMaxWidth()) {
                            Text(if (quest.isJoined) "Leave Quest" else "Join Quest")
                        }
                    }
                }
            }
        }
    }
}