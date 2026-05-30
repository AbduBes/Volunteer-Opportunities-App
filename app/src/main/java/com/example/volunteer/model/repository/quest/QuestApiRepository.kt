package com.example.volunteer.model.repository.quest

import com.example.volunteer.model.data.network.dto.CreateQuestDto
import com.example.volunteer.model.data.network.dto.QuestDto
import com.example.volunteer.model.data.network.dto.UpdateQuestDto

interface QuestApiRepository {
    suspend fun getQuests(): List<QuestDto>
    suspend fun getQuestById(id: Int): QuestDto
    suspend fun createQuest(quest: CreateQuestDto): QuestDto
    suspend fun updateQuest(id: Int, quest: UpdateQuestDto): QuestDto
    suspend fun deleteQuest(id: Int)
}
