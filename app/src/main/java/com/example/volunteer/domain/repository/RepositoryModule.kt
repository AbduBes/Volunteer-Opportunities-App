package com.example.volunteer.domain.repository

import com.example.volunteer.model.repository.AccountRepositoryImpl
import com.example.volunteer.model.repository.CategoryRepositoryImpl
import com.example.volunteer.model.repository.OrganizationRepositoryImpl
import com.example.volunteer.model.repository.QuestRepositoryImpl
import com.example.volunteer.model.repository.UserProfileRepositoryImpl
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
