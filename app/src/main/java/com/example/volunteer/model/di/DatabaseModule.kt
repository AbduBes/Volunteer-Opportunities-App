package com.example.volunteer.model.di

import android.content.Context
import androidx.room.Room
import com.example.volunteer.model.data.local.dao.AccountDao
import com.example.volunteer.model.data.local.dao.CategoryDao
import com.example.volunteer.model.data.local.dao.OrganizationDao
import com.example.volunteer.model.data.local.dao.QuestDao
import com.example.volunteer.model.data.local.dao.UserProfileDao
import com.example.volunteer.model.data.local.dao.UserQuestCrossRefDao
import com.example.volunteer.model.data.local.db.VolunteerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): VolunteerDatabase =
        Room.databaseBuilder(context, VolunteerDatabase::class.java, "volunteer_db")
            .addCallback(VolunteerDatabase.prepopulateCallback)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideAccountDao(db: VolunteerDatabase): AccountDao = db.accountDao()

    @Provides
    fun provideQuestDao(db: VolunteerDatabase): QuestDao = db.questDao()

    @Provides
    fun provideCategoryDao(db: VolunteerDatabase): CategoryDao = db.categoryDao()

    @Provides
    fun provideOrganizationDao(db: VolunteerDatabase): OrganizationDao = db.organizationDao()

    @Provides
    fun provideUserProfileDao(db: VolunteerDatabase): UserProfileDao = db.userProfileDao()

    @Provides
    fun provideUserQuestCrossRefDao(db: VolunteerDatabase): UserQuestCrossRefDao = db.userQuestCrossRefDao()
}
