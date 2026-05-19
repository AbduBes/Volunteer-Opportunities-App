package com.example.volunteer.model.repository

import com.example.volunteer.model.data.local.dao.CategoryDao
import com.example.volunteer.model.data.local.entity.CategoryEntity
import com.example.volunteer.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAll(): Flow<List<CategoryEntity>> =
        categoryDao.getAll()
}
