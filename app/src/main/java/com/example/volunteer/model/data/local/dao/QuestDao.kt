package com.example.volunteer.model.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import com.example.volunteer.model.data.local.entity.CategoryEntity
import com.example.volunteer.model.data.local.entity.OrganizationEntity
import com.example.volunteer.model.data.local.entity.QuestEntity
import kotlinx.coroutines.flow.Flow

data class QuestWithDetails(
    @Embedded val quest: QuestEntity,
    @Relation(parentColumn = "categoryId", entityColumn = "categoryId")
    val category: CategoryEntity,
    @Relation(parentColumn = "organizationId", entityColumn = "organizationId")
    val organization: OrganizationEntity
)

@Dao
interface QuestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quest: QuestEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(quests: List<QuestEntity>)

    @Update
    suspend fun update(quest: QuestEntity)

    @Delete
    suspend fun delete(quest: QuestEntity)

    @Query("DELETE FROM quests WHERE questId = :questId")
    suspend fun deleteById(questId: Int)

    @Transaction
    @Query("SELECT * FROM quests ORDER BY title ASC")
    fun getAllWithDetails(): Flow<List<QuestWithDetails>>

    @Transaction
    @Query(
        "SELECT * FROM quests " +
            "WHERE questId NOT IN (SELECT questId FROM user_quest_cross_ref WHERE userId = :userId) " +
            "AND (creatorId IS NULL OR creatorId <> :userId) " +
            "ORDER BY title ASC"
    )
    fun getAvailableWithDetails(userId: Int): Flow<List<QuestWithDetails>>

    @Transaction
    @Query("SELECT * FROM quests WHERE questId IN (SELECT questId FROM user_quest_cross_ref WHERE userId = :userId) ORDER BY title ASC")
    fun getJoinedWithDetailsForUser(userId: Int): Flow<List<QuestWithDetails>>

    @Transaction
    @Query("SELECT * FROM quests WHERE creatorId = :userId ORDER BY title ASC")
    fun getCreatedWithDetails(userId: Int): Flow<List<QuestWithDetails>>

    @Transaction
    @Query("SELECT * FROM quests WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteWithDetails(): Flow<List<QuestWithDetails>>

    @Transaction
    @Query("SELECT * FROM quests WHERE questId = :questId LIMIT 1")
    suspend fun getWithDetailsById(questId: Int): QuestWithDetails?

    @Query("UPDATE quests SET isJoined = :isJoined WHERE questId = :questId")
    suspend fun updateJoinStatus(questId: Int, isJoined: Boolean)

    @Query("UPDATE quests SET isFavorite = :isFavorite WHERE questId = :questId")
    suspend fun updateFavoriteStatus(questId: Int, isFavorite: Boolean)

    @Query("SELECT COALESCE(MAX(questId), 0) + 1 FROM quests")
    suspend fun nextId(): Int
}
