package com.example.volunteer.model.data.network.dto

data class QuestDto (
    val id: Int,
    val title: String,
    val organization: String,
    val category: String,
    val location: String,
    val hoursRequired: Int,
    val description: String,
    val creatorId: Int? = null
)

data class CreateQuestDto (
    val title: String,
    val organization: String,
    val category: String,
    val location: String,
    val hoursRequired: Int,
    val description: String,
    val creatorId: Int? = null
)

data class UpdateQuestDto (
    val title: String? = null,
    val organization: String? = null,
    val category: String? = null,
    val location: String? = null,
    val hoursRequired: Int? = null,
    val description: String? = null,
    val creatorId: Int? = null
)
