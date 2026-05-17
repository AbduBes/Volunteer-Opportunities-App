package com.example.volunteer.model.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.volunteer.model.data.local.entity.OrganizationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrganizationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(organizations: List<OrganizationEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(organization: OrganizationEntity): Long

    @Query("SELECT * FROM organizations ORDER BY name ASC")
    fun getAll(): Flow<List<OrganizationEntity>>

    @Query("SELECT * FROM organizations WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): OrganizationEntity?

    @Query("SELECT COALESCE(MAX(organizationId), 0) + 1 FROM organizations")
    suspend fun nextId(): Int
}
