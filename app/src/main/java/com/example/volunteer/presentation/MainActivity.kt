package com.example.volunteer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.volunteer.presentation.navigation.NavGraph
import com.example.volunteer.presentation.navigation.Screen
import com.example.volunteer.presentation.theme.VolunteerTheme
import com.example.volunteer.presentation.ui.AppDestinations
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    // Show bottom bar only on these top-level screens
    val showBottomBar = currentRoute in listOf(
        Screen.HOME.route,
        Screen.QUESTS.route,
        Screen.PROFILE.route
    )

    if (showBottomBar) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach { dest ->
                    val destRoute = when (dest) {
                        AppDestinations.HOME -> Screen.HOME.route
                        AppDestinations.QUESTS -> Screen.QUESTS.route
                        AppDestinations.PROFILE -> Screen.PROFILE.route
                    }

                    item(
                        icon = { Icon(painterResource(dest.icon), contentDescription = dest.label) },
                        label = { Text(dest.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == destRoute } == true,
                        onClick = {
                            navController.navigate(destRoute) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        ) {
            MainScaffoldContent(navController, currentRoute)
        }
    } else {
        // Render without the bottom navigation bar for details/add screens
        MainScaffoldContent(navController, currentRoute)
    }
}

@Composable
fun MainScaffoldContent(navController: NavHostController, currentRoute: String?) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (currentRoute == Screen.QUESTS.route) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.ADD_QUEST.route) }
                ) {
                    Text("+")
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
