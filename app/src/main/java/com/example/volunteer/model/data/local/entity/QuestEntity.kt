package com.example.volunteer.model.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "quests",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = OrganizationEntity::class,
            parentColumns = ["organizationId"],
            childColumns = ["organizationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("categoryId"), Index("organizationId")]
)
data class QuestEntity(
    @PrimaryKey val questId: Int,
    val title: String,
    val organizationId: Int,
    val categoryId: Int,
    val location: String,
    val hoursRequired: Int,
    val description: String,
    val isJoined: Boolean = false,
    val isFavorite: Boolean = false,
    val creatorId: Int? = null
)
