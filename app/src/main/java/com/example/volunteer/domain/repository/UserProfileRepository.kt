package com.example.volunteer.domain.repository

import com.example.volunteer.model.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun getFirst(): Flow<UserProfileEntity?>
    fun getById(userId: Int): Flow<UserProfileEntity?>
    suspend fun update(profile: UserProfileEntity)
}
