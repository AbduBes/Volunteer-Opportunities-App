package com.example.volunteer.model.data.repository

import com.example.volunteer.model.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAll(): Flow<List<CategoryEntity>>
}
