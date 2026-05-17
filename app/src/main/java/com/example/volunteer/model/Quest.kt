package com.example.volunteer.model

data class Quest(
    val id: Int,
    val title: String,
    val organization: String,
    val category: String,
    val location: String,
    val hoursRequired: Int,
    val description: String,
    val isJoined: Boolean = false,
    val isFavorite: Boolean = false,
    val creatorId: Int? = null
)