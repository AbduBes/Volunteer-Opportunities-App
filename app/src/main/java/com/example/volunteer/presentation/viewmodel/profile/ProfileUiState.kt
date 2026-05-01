package com.example.volunteer.presentation.viewmodel.profile

sealed interface ProfileUiState {
    data object Loading : ProfileUiState

    data class Success(
        val name: String = "Abdurrahman Besirovic",
        val email: String = "abdurrahman.besirovic@stu.ibu.edu.ba",
        val bio: String = "Passionate about helping the community.",
        val levelNo: String = "3",
        val levelDescription: String = "Adventurer",
        val totalHours: Int = 24,
        val questsCompleted: Int = 7,
        val streak: Int = 5,
        val badges: List<String> = listOf("First Quest", "10 Hours", "Eco Warrior", "Team Player")
    ) : ProfileUiState {
        val userLevel: Int
            get() = (totalHours / 10) + 1

        val isPowerVolunteer: Boolean
            get() = questsCompleted > 5
    }

    data class Error(val message: String) : ProfileUiState
}