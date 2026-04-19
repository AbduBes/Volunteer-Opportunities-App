package com.example.volunteer.presentation.ui.screens.detail

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
import com.example.volunteer.presentation.ui.screens.detail.components.DetailInfoRow
import com.example.volunteer.presentation.viewmodel.QuestsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestDetailScreen(
    questId: Int,
    source: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuestsViewModel = viewModel()
) {
    val quest = viewModel.getQuestById(questId)
    var joined by remember { mutableStateOf(quest?.isJoined ?: false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(quest?.title ?: "Quest Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (quest == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Quest not found.")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
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
                    onClick = { joined = !joined },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (joined) "Leave Quest" else "Join Quest")
                }
            }
        }
    }
}