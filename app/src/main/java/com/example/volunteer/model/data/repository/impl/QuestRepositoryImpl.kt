package com.example.volunteer.model.data.repository.impl

import com.example.volunteer.model.data.local.dao.QuestDao
import com.example.volunteer.model.data.local.dao.QuestWithDetails
import com.example.volunteer.model.data.local.dao.UserQuestCrossRefDao
import com.example.volunteer.model.data.local.entity.QuestEntity
import com.example.volunteer.model.data.local.entity.UserQuestCrossRef
import com.example.volunteer.model.data.repository.QuestRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuestRepositoryImpl @Inject constructor(
    private val questDao: QuestDao,
    private val userQuestCrossRefDao: UserQuestCrossRefDao
) : QuestRepository {

    override fun getAllWithDetails(): Flow<List<QuestWithDetails>> =
        questDao.getAllWithDetails()

    override fun getAvailableWithDetails(userId: Int): Flow<List<QuestWithDetails>> =
        questDao.getAvailableWithDetails(userId)

    override fun getJoinedWithDetailsForUser(userId: Int): Flow<List<QuestWithDetails>> =
        questDao.getJoinedWithDetailsForUser(userId)

    override fun getCreatedWithDetails(userId: Int): Flow<List<QuestWithDetails>> =
        questDao.getCreatedWithDetails(userId)

    override suspend fun getWithDetailsById(questId: Int): QuestWithDetails? =
        questDao.getWithDetailsById(questId)

    override suspend fun isJoinedByUser(userId: Int, questId: Int): Boolean =
        userQuestCrossRefDao.isJoinedByUser(userId, questId)

    override suspend fun insert(quest: QuestEntity) =
        questDao.insert(quest)

    override suspend fun toggleJoin(userId: Int, questId: Int, isJoined: Boolean) {
        questDao.updateJoinStatus(questId, isJoined)
        if (isJoined) {
            userQuestCrossRefDao.insert(UserQuestCrossRef(userId, questId))
        } else {
            userQuestCrossRefDao.delete(UserQuestCrossRef(userId, questId))
        }
    }

    override suspend fun updateJoinStatus(questId: Int, isJoined: Boolean) =
        questDao.updateJoinStatus(questId, isJoined)

    override suspend fun updateFavoriteStatus(questId: Int, isFavorite: Boolean) =
        questDao.updateFavoriteStatus(questId, isFavorite)

    override suspend fun deleteById(questId: Int) =
        questDao.deleteById(questId)

    override suspend fun nextId(): Int =
        questDao.nextId()
}
