package com.example.volunteer.domain.repository

import com.example.volunteer.model.data.local.dao.QuestWithDetails
import com.example.volunteer.model.data.local.entity.QuestEntity
import kotlinx.coroutines.flow.Flow

interface QuestRepository {
    fun getAllWithDetails(): Flow<List<QuestWithDetails>>
    fun getAvailableWithDetails(userId: Int): Flow<List<QuestWithDetails>>
    fun getJoinedWithDetailsForUser(userId: Int): Flow<List<QuestWithDetails>>
    fun getCreatedWithDetails(userId: Int): Flow<List<QuestWithDetails>>
    suspend fun getWithDetailsById(questId: Int): QuestWithDetails?
    suspend fun isJoinedByUser(userId: Int, questId: Int): Boolean
    suspend fun insert(quest: QuestEntity)
    suspend fun toggleJoin(userId: Int, questId: Int, isJoined: Boolean)
    suspend fun updateJoinStatus(questId: Int, isJoined: Boolean)
    suspend fun updateFavoriteStatus(questId: Int, isFavorite: Boolean)
    suspend fun deleteById(questId: Int)
    suspend fun nextId(): Int
}
