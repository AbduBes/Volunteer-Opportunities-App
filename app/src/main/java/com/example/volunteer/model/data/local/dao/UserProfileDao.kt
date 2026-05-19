package com.example.volunteer.model.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.volunteer.model.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(profile: UserProfileEntity)

    @Update
    suspend fun update(profile: UserProfileEntity)

    @Query("SELECT * FROM user_profiles WHERE userId = :userId LIMIT 1")
    fun getById(userId: Int): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profiles LIMIT 1")
    fun getFirst(): Flow<UserProfileEntity?>

    @Query("UPDATE user_profiles SET totalHours = totalHours + :hours WHERE userId = :userId")
    suspend fun addHours(userId: Int, hours: Int)

    @Query("UPDATE user_profiles SET questsCompleted = questsCompleted + 1 WHERE userId = :userId")
    suspend fun incrementQuestsCompleted(userId: Int)
}
