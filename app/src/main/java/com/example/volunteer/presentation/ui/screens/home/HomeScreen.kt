package com.example.volunteer.presentation.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.volunteer.presentation.ui.screens.home.components.ActiveQuestCard
import com.example.volunteer.presentation.ui.screens.home.components.StatCard
import com.example.volunteer.presentation.viewmodel.home.HomeEvent
import com.example.volunteer.presentation.viewmodel.home.HomeUiState
import com.example.volunteer.presentation.viewmodel.home.HomeViewModel

@Composable
fun HomeScreen(
    onViewQuestDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event) {
                is HomeEvent.NavigateToQuestDetail -> onViewQuestDetail(event.questId)
            }

        }
    }

    HomeScreenContent(
        uiState = uiState,
        onQuestClick = viewModel::onQuestClicked,
        modifier = modifier
    )
}

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    onQuestClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is HomeUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is HomeUiState.Success -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Text(
                        text = "Welcome back, ${uiState.userName} \uD83D\uDC4B",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Here's your volunteer summary",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            label = "Hours Given",
                            value = "${uiState.totalHours}h",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Quests Done",
                            value = "${uiState.questsCompleted}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Text(
                        text = "Active Quests",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                if (uiState.activeQuests.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = androidx.compose.ui.Alignment.Center
                            ) {
                                Text(
                                    text = "No active quests yet.\nJoin one from the Quests tab!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(uiState.activeQuests) { quest ->
                        // Pass the action back up to the stateless component parameters
                        ActiveQuestCard(quest = quest, onViewDetail = onQuestClick)
                    }
                }
            }
        }
    }
}