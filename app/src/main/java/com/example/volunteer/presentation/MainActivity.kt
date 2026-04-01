package com.example.volunteer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.volunteer.R
import com.example.volunteer.presentation.theme.VolunteerTheme
import com.example.volunteer.presentation.ui.AppDestinations
import com.example.volunteer.presentation.ui.screens.add.AddQuestScreen
import com.example.volunteer.presentation.ui.screens.detail.QuestDetailScreen
import com.example.volunteer.presentation.ui.screens.home.HomeScreen
import com.example.volunteer.presentation.ui.screens.profile.ProfileScreen
import com.example.volunteer.presentation.ui.screens.quests.QuestsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VolunteerTheme {
                VolunteerApp()
            }
        }
    }
}

@Composable
fun VolunteerApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var selectedQuestId by rememberSaveable { mutableStateOf<Int?>(null) }
    var showAddQuest by rememberSaveable { mutableStateOf(false) }

    when {
        selectedQuestId != null -> {
            QuestDetailScreen(
                questId = selectedQuestId!!,
                onBack = { selectedQuestId = null }
            )
        }
        showAddQuest -> {
            AddQuestScreen(onBack = { showAddQuest = false })
        }
        else -> {
            NavigationSuiteScaffold(
                navigationSuiteItems = {
                    AppDestinations.entries.forEach { dest ->
                        item(
                            icon = {
                                Icon(
                                    painterResource(dest.icon),
                                    contentDescription = dest.label
                                )
                            },
                            label = { Text(dest.label) },
                            selected = dest == currentDestination,
                            onClick = { currentDestination = dest }
                        )
                    }
                }
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        if (currentDestination == AppDestinations.QUESTS) {
                            androidx.compose.material3.FloatingActionButton(
                                onClick = { showAddQuest = true }
                            ) {
                                Text("+")
                            }
                        }
                    }
                ) { innerPadding ->
                    val contentModifier = Modifier.padding(innerPadding)
                    when (currentDestination) {
                        AppDestinations.HOME -> HomeScreen(
                            onViewQuestDetail = { selectedQuestId = it },
                            modifier = contentModifier
                        )
                        AppDestinations.QUESTS -> QuestsScreen(
                            onViewDetail = { selectedQuestId = it },
                            modifier = contentModifier
                        )
                        AppDestinations.PROFILE -> ProfileScreen(modifier = contentModifier)
                    }
                }
            }
        }
    }
}