package com.example.volunteer.model.data.network.service

import com.example.volunteer.model.data.network.dto.CreateQuestDto
import com.example.volunteer.model.data.network.dto.QuestDto
import com.example.volunteer.model.data.network.dto.UpdateQuestDto
import retrofit2.http.*

interface QuestService {
    @GET("quests/")
    suspend fun getQuests(
        @Header("X-Authentication") authHeader: String = "yes"
    ): List<QuestDto>

    @GET("quests/{id}")
    suspend fun getQuestById(
        @Path("id") id: Int,
        @Header("X-Authentication") authHeader: String = "yes"
    ): QuestDto

    @POST("quests/")
    suspend fun createQuest(
        @Body quest: CreateQuestDto,
        @Header("X-Authentication") authHeader: String = "yes"
    ): QuestDto

    @PUT("quests/{id}")
    suspend fun updateQuest(
        @Path("id") id: Int,
        @Body quest: UpdateQuestDto,
        @Header("X-Authentication") authHeader: String = "yes"
    ): QuestDto

    @DELETE("quests/{id}")
    suspend fun deleteQuest(
        @Path("id") id: Int,
        @Header("X-Authentication") authHeader: String = "yes"
    )
}