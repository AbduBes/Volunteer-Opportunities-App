package com.example.volunteer.model.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.volunteer.model.data.local.entity.UserQuestCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface UserQuestCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crossRef: UserQuestCrossRef)

    @Delete
    suspend fun delete(crossRef: UserQuestCrossRef)

    @Query("SELECT * FROM user_quest_cross_ref WHERE userId = :userId")
    fun getCrossRefsForUser(userId: Int): Flow<List<UserQuestCrossRef>>

    @Query("SELECT questId FROM user_quest_cross_ref WHERE userId = :userId")
    suspend fun getCompletedQuestIdsForUser(userId: Int): List<Int>

    @Query("SELECT COUNT(*) FROM user_quest_cross_ref WHERE userId = :userId")
    fun getCompletionCountForUser(userId: Int): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM user_quest_cross_ref WHERE userId = :userId AND questId = :questId)")
    suspend fun isJoinedByUser(userId: Int, questId: Int): Boolean
}
