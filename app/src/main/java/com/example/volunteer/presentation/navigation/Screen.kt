package com.example.volunteer.presentation.navigation

sealed class Screen(val route: String) {
    data object AUTH : Screen("auth_screen")
    data object HOME : Screen("home_screen")
    data object ADD_QUEST : Screen("add_quest_screen")
    data object PROFILE : Screen("profile_screen")
    data object QUESTS : Screen("quests_screen")

    data object QUEST_DETAIL : Screen("quest_detail_screen/{questId}/{source}") {
        fun createRoute(questId: Int, source: String) = "quest_detail_screen/$questId/$source"
    }
}
