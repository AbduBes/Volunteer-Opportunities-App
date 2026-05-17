package com.example.volunteer.model.data.repository.impl

import com.example.volunteer.model.data.local.dao.OrganizationDao
import com.example.volunteer.model.data.local.entity.OrganizationEntity
import com.example.volunteer.model.data.repository.OrganizationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrganizationRepositoryImpl @Inject constructor(
    private val organizationDao: OrganizationDao
) : OrganizationRepository {

    override fun getAll(): Flow<List<OrganizationEntity>> =
        organizationDao.getAll()

    override suspend fun getByName(name: String): OrganizationEntity? =
        organizationDao.getByName(name)

    override suspend fun insert(organization: OrganizationEntity): Long =
        organizationDao.insert(organization)

    override suspend fun nextId(): Int =
        organizationDao.nextId()
}
