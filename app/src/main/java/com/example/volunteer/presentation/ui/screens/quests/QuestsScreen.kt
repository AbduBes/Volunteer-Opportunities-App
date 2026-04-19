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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.volunteer.presentation.ui.screens.quests.components.QuestItem
import com.example.volunteer.presentation.viewmodel.QuestsViewModel

@Composable
fun QuestsScreen(
    onListClick: () -> Unit,
    onGridClick: () -> Unit,
    onViewDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuestsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var isGroupedLayout by remember { mutableStateOf(false) }

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

                // 2. Connect List button
                IconButton(onClick = {
                    isGroupedLayout = false
                    onListClick()
                }) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "List View",
                        // Highlight icon if selected
                        tint = if (!isGroupedLayout) MaterialTheme.colorScheme.primary else LocalContentColor.current
                    )
                }

                // 3. Connect Grid/Row button
                IconButton(onClick = {
                    isGroupedLayout = true
                    onGridClick()
                }) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Grid/Card View",
                        // Highlight icon if selected
                        tint = if (isGroupedLayout) MaterialTheme.colorScheme.primary else LocalContentColor.current
                    )
                }
            }
        }

        // Category Filter Chips (This counts as your FIRST LazyRow!)
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.categories) { category ->
                    FilterChip(
                        selected = state.selectedCategory == category,
                        onClick = { viewModel.selectCategory(category) },
                        label = { Text(category) }
                    )
                }
            }
        }

        if (state.allQuests.isEmpty()) {
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
            // 4. Toggle between standard list and grouped rows
            if (!isGroupedLayout) {
                // STANDARD LIST LAYOUT
                items(state.allQuests) { quest ->
                    QuestItem(
                        quest = quest,
                        onFavoriteToggle = { viewModel.toggleFavorite(it) },
                        onViewDetail = onViewDetail
                    )
                }
            } else {
                // GROUPED ROWS LAYOUT (Categories)
                val groupedQuests = state.allQuests.groupBy { it.category }

                groupedQuests.forEach { (category, quests) ->
                    if (quests.isNotEmpty()) {
                        // Category Title
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
                                        onFavoriteToggle = { viewModel.toggleFavorite(it) },
                                        onViewDetail = onViewDetail,
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