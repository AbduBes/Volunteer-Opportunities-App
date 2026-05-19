package com.example.volunteer.model.data.network.mapper

import com.example.volunteer.domain.data.Quest
import com.example.volunteer.model.data.network.dto.QuestDto

fun QuestDto.toQuest(): Quest = Quest(
    id = id,
    title = title,
    organization = organization,
    category = category,
    location = location,
    hoursRequired = hoursRequired,
    description = description,
    creatorId = creatorId
)
