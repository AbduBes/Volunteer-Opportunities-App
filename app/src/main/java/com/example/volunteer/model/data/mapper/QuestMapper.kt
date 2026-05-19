package com.example.volunteer.model.data.mapper

import com.example.volunteer.model.Quest
import com.example.volunteer.model.data.local.dao.QuestWithDetails

fun QuestWithDetails.toQuest() = Quest(
    id = quest.questId,
    title = quest.title,
    organization = organization.name,
    category = category.name,
    location = quest.location,
    hoursRequired = quest.hoursRequired,
    description = quest.description,
    isJoined = quest.isJoined,
    isFavorite = quest.isFavorite,
    creatorId = quest.creatorId
)
