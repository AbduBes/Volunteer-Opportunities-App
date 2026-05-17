package com.example.volunteer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.volunteer.presentation.ui.screens.add.AddQuestScreen
import com.example.volunteer.presentation.ui.screens.auth.AuthScreen
import com.example.volunteer.presentation.ui.screens.detail.QuestDetailScreen
import com.example.volunteer.presentation.ui.screens.home.HomeScreen
import com.example.volunteer.presentation.ui.screens.profile.ProfileScreen
import com.example.volunteer.presentation.ui.screens.quests.QuestsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.AUTH.route,
        modifier = modifier
    ) {
        // --- Auth (no bottom nav) ---

        composable(route = Screen.AUTH.route) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Screen.HOME.route) {
                        popUpTo(Screen.AUTH.route) { inclusive = true }
                    }
                }
            )
        }

        // --- Bottom Nav Destinations ---

        composable(route = Screen.HOME.route) {
            HomeScreen(
                onViewQuestDetail = { questId ->
                    navController.navigate(Screen.QUEST_DETAIL.createRoute(questId, "Home"))
                }
            )
        }

        composable(route = Screen.QUESTS.route) {
            QuestsScreen(
                onViewDetail = { questId ->
                    navController.navigate(Screen.QUEST_DETAIL.createRoute(questId, "Quests"))
                }
            )
        }

        composable(route = Screen.PROFILE.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onLoggedOut = {
                    navController.navigate(Screen.AUTH.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // --- Detail / Sub-screens (no bottom nav) ---

        composable(route = Screen.ADD_QUEST.route) {
            AddQuestScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.QUEST_DETAIL.route,
            arguments = listOf(
                navArgument("questId") { type = NavType.IntType },
                navArgument("source") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val questId = backStackEntry.arguments?.getInt("questId") ?: return@composable
            val source = backStackEntry.arguments?.getString("source") ?: "Unknown"

            QuestDetailScreen(
                questId = questId,
                source = source,
                onBack = { navController.popBackStack() }
            )
        }
    }
}