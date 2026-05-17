package com.example.volunteer.model.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val userId: Int,
    val name: String,
    val email: String,
    val bio: String,
    val totalHours: Int,
    val questsCompleted: Int,
    val streak: Int
)
