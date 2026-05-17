package com.example.volunteer.presentation.ui

import com.example.volunteer.R

enum class AppDestinations(
    val label: String,
    val icon: Int
) {
    HOME("Home", R.drawable.ic_home),
    QUESTS("Quests", R.drawable.ic_favorite),
    PROFILE("Profile", R.drawable.ic_account_box)
}