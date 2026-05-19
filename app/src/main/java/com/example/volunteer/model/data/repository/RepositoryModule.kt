package com.example.volunteer.model.data.repository

import com.example.volunteer.model.data.repository.impl.AccountRepositoryImpl
import com.example.volunteer.model.data.repository.impl.CategoryRepositoryImpl
import com.example.volunteer.model.data.repository.impl.OrganizationRepositoryImpl
import com.example.volunteer.model.data.repository.impl.QuestRepositoryImpl
import com.example.volunteer.model.data.repository.impl.UserProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    @Singleton
    abstract fun bindQuestRepository(impl: QuestRepositoryImpl): QuestRepository

    @Binds
    @Singleton
    abstract fun bindUserProfileRepository(impl: UserProfileRepositoryImpl): UserProfileRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindOrganizationRepository(impl: OrganizationRepositoryImpl): OrganizationRepository
}
