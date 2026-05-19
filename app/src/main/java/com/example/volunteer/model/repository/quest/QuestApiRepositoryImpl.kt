package com.example.volunteer.model.repository.quest

import com.example.volunteer.model.data.network.dto.CreateQuestDto
import com.example.volunteer.model.data.network.dto.QuestDto
import com.example.volunteer.model.data.network.dto.UpdateQuestDto
import com.example.volunteer.model.data.network.service.QuestService

class QuestApiRepositoryImpl(
    private val api: QuestService
) : QuestApiRepository {

    override suspend fun getQuests(): List<QuestDto> = api.getQuests()

    override suspend fun getQuestById(id: Int): QuestDto = api.getQuestById(id)

    override suspend fun createQuest(quest: CreateQuestDto): QuestDto = api.createQuest(quest)

    override suspend fun updateQuest(id: Int, quest: UpdateQuestDto): QuestDto =
        api.updateQuest(id, quest)

    override suspend fun deleteQuest(id: Int) = api.deleteQuest(id)
}
