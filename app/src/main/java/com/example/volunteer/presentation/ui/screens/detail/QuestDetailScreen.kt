package com.example.volunteer.presentation.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.volunteer.presentation.ui.screens.detail.components.DetailInfoRow
import com.example.volunteer.presentation.viewmodel.questDetail.QuestDetailEvent
import com.example.volunteer.presentation.viewmodel.questDetail.QuestDetailUiState
import com.example.volunteer.presentation.viewmodel.questDetail.QuestDetailViewModel

// 1. Stateful Component (Logic & Integration)
@Composable
fun QuestDetailScreen(
    questId: Int,
    source: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuestDetailViewModel = hiltViewModel() // Use correct ViewModel via Hilt
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Fetch the quest when the screen is composed or if the ID changes
    LaunchedEffect(questId) {
        viewModel.loadQuest(questId)
    }

    // Handle one-time Channel events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is QuestDetailEvent.NavigateBack -> onBack()
                is QuestDetailEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    QuestDetailScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = viewModel::navigateBack,
        onJoinToggle = viewModel::toggleJoin,
        modifier = modifier
    )
}

// 2. Stateless Component (Pure UI)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestDetailScreenContent(
    uiState: QuestDetailUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onJoinToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (uiState is QuestDetailUiState.Success) uiState.quest.title else "Quest Detail")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is QuestDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is QuestDetailUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is QuestDetailUiState.Success -> {
                    val quest = uiState.quest

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = quest.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = quest.organization,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        DetailInfoRow(label = "Category", value = quest.category)
                        DetailInfoRow(label = "Location", value = quest.location)
                        DetailInfoRow(label = "Hours Required", value = "${quest.hoursRequired} hours")

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "About this Quest",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = quest.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        Button(
                            onClick = onJoinToggle,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Automatically reads updated state from the ViewModel
                            Text(if (quest.isJoined) "Leave Quest" else "Join Quest")
                        }
                    }
                }
            }
        }
    }
}