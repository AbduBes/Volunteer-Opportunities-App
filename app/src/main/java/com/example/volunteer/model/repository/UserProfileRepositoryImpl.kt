package com.example.volunteer.model.repository

import com.example.volunteer.model.data.local.dao.UserProfileDao
import com.example.volunteer.model.data.local.entity.UserProfileEntity
import com.example.volunteer.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val userProfileDao: UserProfileDao
) : UserProfileRepository {

    override fun getFirst(): Flow<UserProfileEntity?> =
        userProfileDao.getFirst()

    override fun getById(userId: Int): Flow<UserProfileEntity?> =
        userProfileDao.getById(userId)

    override suspend fun update(profile: UserProfileEntity) =
        userProfileDao.update(profile)
}
