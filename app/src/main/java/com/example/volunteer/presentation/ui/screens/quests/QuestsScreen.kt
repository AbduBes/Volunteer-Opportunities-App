package com.example.volunteer.presentation.ui.screens.quests

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.volunteer.presentation.ui.screens.quests.components.QuestItem
import com.example.volunteer.presentation.viewmodel.quest.QuestsEvent
import com.example.volunteer.presentation.viewmodel.quest.QuestsUiState
import com.example.volunteer.presentation.viewmodel.quest.QuestsViewModel

// 1. Stateful Component (Logic & Integration)
@Composable
fun QuestsScreen(
    onListClick: () -> Unit,
    onGridClick: () -> Unit,
    onViewDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuestsViewModel = hiltViewModel() // Use Hilt
) {
    // Lifecycle-aware state collection
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle one-time events centrally
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is QuestsEvent.NavigateToQuestDetail -> onViewDetail(event.questId)
                is QuestsEvent.NavigateToList -> onListClick()
                is QuestsEvent.NavigateToGrid -> onGridClick()
            }
        }
    }

    QuestsScreenContent(
        uiState = uiState,
        onCategorySelected = viewModel::selectCategory,
        onFavoriteToggled = viewModel::toggleFavorite,
        onQuestClicked = viewModel::onQuestClicked,
        onLayoutToggled = viewModel::setGroupedLayout,
        modifier = modifier
    )
}

// 2. Stateless Component (Pure UI)
@Composable
private fun QuestsScreenContent(
    uiState: QuestsUiState,
    onCategorySelected: (String) -> Unit,
    onFavoriteToggled: (Int) -> Unit,
    onQuestClicked: (Int) -> Unit,
    onLayoutToggled: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is QuestsUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is QuestsUiState.Success -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Volunteer Quests",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(onClick = { onLayoutToggled(false) }) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "List View",
                                tint = if (!uiState.isGroupedLayout) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        }

                        IconButton(onClick = { onLayoutToggled(true) }) {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = "Grid/Card View",
                                tint = if (uiState.isGroupedLayout) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        }
                    }
                }

                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.categories) { category ->
                            FilterChip(
                                selected = uiState.selectedCategory == category,
                                onClick = { onCategorySelected(category) },
                                label = { Text(category) }
                            )
                        }
                    }
                }

                if (uiState.allQuests.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No quests in this category yet.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                } else {
                    if (!uiState.isGroupedLayout) {
                        // STANDARD LIST LAYOUT
                        items(uiState.allQuests) { quest ->
                            QuestItem(
                                quest = quest,
                                onFavoriteToggle = { onFavoriteToggled(it) },
                                onViewDetail = onQuestClicked
                            )
                        }
                    } else {
                        // GROUPED ROWS LAYOUT (Categories)
                        val groupedQuests = uiState.allQuests.groupBy { it.category }

                        groupedQuests.forEach { (category, quests) ->
                            if (quests.isNotEmpty()) {
                                item {
                                    Text(
                                        text = category,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                    )
                                }

                                item {
                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        contentPadding = PaddingValues(bottom = 8.dp)
                                    ) {
                                        items(quests) { quest ->
                                            QuestItem(
                                                quest = quest,
                                                onFavoriteToggle = { onFavoriteToggled(it) },
                                                onViewDetail = onQuestClicked,
                                                modifier = Modifier.width(300.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}