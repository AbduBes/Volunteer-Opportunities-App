package com.example.volunteer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.volunteer.presentation.ui.screens.add.AddQuestScreen
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
        startDestination = Screen.HOME.route,
        modifier = modifier
    ) {
        composable(route = Screen.HOME.route) {
            HomeScreen(
                onViewQuestDetail = { questId -> // Only takes 1 argument from HomeScreen
                    // But we pass 2 arguments to the Route!
                    navController.navigate(Screen.QUEST_DETAIL.createRoute(questId, "Home"))
                }
            )
        }

        composable(route = Screen.QUESTS.route) {
            QuestsScreen(
                onListClick = {},
                onGridClick = {},
                onViewDetail = { questId -> // Only takes 1 argument from QuestsScreen
                    navController.navigate(Screen.QUEST_DETAIL.createRoute(questId, "Quests"))
                }
            )
        }

        composable(route = Screen.PROFILE.route) {
            ProfileScreen()
        }

        composable(route = Screen.ADD_QUEST.route) {
            AddQuestScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Navigate with arguments
        composable(
            route = Screen.QUEST_DETAIL.route,
            arguments = listOf(
                navArgument("questId") { type = NavType.IntType },
                navArgument("source") { type = NavType.StringType } // Second argument
            )
        ) { backStackEntry ->
            val questId = backStackEntry.arguments?.getInt("questId") ?: return@composable
            val source = backStackEntry.arguments?.getString("source") ?: "Unknown"

            QuestDetailScreen(
                questId = questId,
                source = source, // Pass it to the screen
                onBack = { navController.popBackStack() }
            )
        }
    }
}