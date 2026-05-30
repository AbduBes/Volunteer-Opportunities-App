package com.example.volunteer.domain.repository

import com.example.volunteer.model.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAll(): Flow<List<CategoryEntity>>
}
