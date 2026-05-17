package com.example.volunteer.model.data.repository

import com.example.volunteer.model.data.local.entity.OrganizationEntity
import kotlinx.coroutines.flow.Flow

interface OrganizationRepository {
    fun getAll(): Flow<List<OrganizationEntity>>
    suspend fun getByName(name: String): OrganizationEntity?
    suspend fun insert(organization: OrganizationEntity): Long
    suspend fun nextId(): Int
}
