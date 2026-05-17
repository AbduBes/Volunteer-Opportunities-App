package com.example.volunteer.model.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.volunteer.model.data.local.dao.AccountDao
import com.example.volunteer.model.data.local.dao.CategoryDao
import com.example.volunteer.model.data.local.dao.OrganizationDao
import com.example.volunteer.model.data.local.dao.QuestDao
import com.example.volunteer.model.data.local.dao.UserProfileDao
import com.example.volunteer.model.data.local.dao.UserQuestCrossRefDao
import com.example.volunteer.model.data.local.entity.AccountEntity
import com.example.volunteer.model.data.local.entity.CategoryEntity
import com.example.volunteer.model.data.local.entity.OrganizationEntity
import com.example.volunteer.model.data.local.entity.QuestEntity
import com.example.volunteer.model.data.local.entity.UserProfileEntity
import com.example.volunteer.model.data.local.entity.UserQuestCrossRef
import com.example.volunteer.model.data.local.util.Converters

@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        OrganizationEntity::class,
        QuestEntity::class,
        UserProfileEntity::class,
        UserQuestCrossRef::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class VolunteerDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun organizationDao(): OrganizationDao
    abstract fun questDao(): QuestDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun userQuestCrossRefDao(): UserQuestCrossRefDao

    companion object {
        // Seeds on every open if the database is empty. This covers a fresh
        // install and a destructive migration alike — the onDestructiveMigration
        // callback fires before tables are recreated, so it cannot be used here.
        val prepopulateCallback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                if (isEmpty(db)) {
                    seed(db)
                }
            }
        }

        private fun isEmpty(db: SupportSQLiteDatabase): Boolean =
            db.query("SELECT COUNT(*) FROM categories").use { cursor ->
                !cursor.moveToFirst() || cursor.getInt(0) == 0
            }

        private fun seed(db: SupportSQLiteDatabase) {
            // Categories
            db.execSQL("INSERT INTO categories VALUES (1, 'Environment')")
            db.execSQL("INSERT INTO categories VALUES (2, 'Food')")
            db.execSQL("INSERT INTO categories VALUES (3, 'Animals')")
            db.execSQL("INSERT INTO categories VALUES (4, 'Education')")
            db.execSQL("INSERT INTO categories VALUES (5, 'Healthcare')")

            // Organizations
            db.execSQL("INSERT INTO organizations VALUES (1, 'Green City')")
            db.execSQL("INSERT INTO organizations VALUES (2, 'Community Aid')")
            db.execSQL("INSERT INTO organizations VALUES (3, 'Paws & Care')")
            db.execSQL("INSERT INTO organizations VALUES (4, 'City Library')")
            db.execSQL("INSERT INTO organizations VALUES (5, 'Elder Care')")
            db.execSQL("INSERT INTO organizations VALUES (6, 'Ocean Friends')")
            db.execSQL("INSERT INTO organizations VALUES (7, 'Tech4Good')")

            // Quests (questId, title, organizationId, categoryId, location, hoursRequired, description, isJoined, isFavorite, creatorId)
            // creatorId NULL = seed quest with no owner; quests 1 & 7 belong to user 1, quests 4 & 5 to user 2.
            db.execSQL("INSERT INTO quests VALUES (1, 'Park Cleanup', 1, 1, 'City Park', 3, 'Help clean the local park and plant new flowers.', 0, 0, 1)")
            db.execSQL("INSERT INTO quests VALUES (2, 'Food Bank Helper', 2, 2, 'Downtown', 4, 'Sort and distribute food donations to families in need.', 1, 0, NULL)")
            db.execSQL("INSERT INTO quests VALUES (3, 'Animal Shelter', 3, 3, 'West Side', 2, 'Walk and care for shelter animals awaiting adoption.', 1, 0, NULL)")
            db.execSQL("INSERT INTO quests VALUES (4, 'Reading Buddy', 4, 4, 'Main Library', 2, 'Read books with children aged 5–10 every Saturday.', 0, 0, 2)")
            db.execSQL("INSERT INTO quests VALUES (5, 'Senior Visit', 5, 5, 'Sunrise Home', 3, 'Visit and chat with elderly residents.', 0, 0, 2)")
            db.execSQL("INSERT INTO quests VALUES (6, 'Beach Cleanup', 6, 1, 'South Beach', 5, 'Remove plastic waste from the coastline.', 0, 0, NULL)")
            db.execSQL("INSERT INTO quests VALUES (7, 'Coding Workshop', 7, 4, 'Community Center', 4, 'Teach basic coding to teenagers.', 0, 0, 1)")

            // User profiles (userId, name, email, bio, totalHours, questsCompleted, streak)
            db.execSQL("INSERT INTO user_profiles VALUES (1, 'Abdurrahman Besirovic', 'abdurrahman.besirovic@stu.ibu.edu.ba', 'Passionate about helping the community.', 24, 7, 5)")
            db.execSQL("INSERT INTO user_profiles VALUES (2, 'Maria Hodzic', 'maria@example.com', 'New volunteer exploring the city.', 12, 3, 2)")

            // Demo accounts — both use the password "demo1234"
            db.execSQL(
                "INSERT INTO accounts (accountId, email, passwordHash, salt, name) VALUES " +
                    "(1, 'abdurrahman.besirovic@stu.ibu.edu.ba', " +
                    "'5EUnnn07/768fYjfllJbkWAVnHEn48qeKkFGGFV/CcE=', " +
                    "'wwmBTIDdkDqRxUXpVpXEOQ==', 'Abdurrahman Besirovic')"
            )
            db.execSQL(
                "INSERT INTO accounts (accountId, email, passwordHash, salt, name) VALUES " +
                    "(2, 'maria@example.com', " +
                    "'A64e69nWFiRr+tWaBdWFQ28eHqoEDbD4SS+Wn12hdHI=', " +
                    "'82xtcWCPKWlIB8bKAGYX9w==', 'Maria Hodzic')"
            )

            // Joined-quest cross-refs (userId, questId, completedAt)
            val now = System.currentTimeMillis()
            db.execSQL("INSERT INTO user_quest_cross_ref VALUES (1, 2, $now)")
            db.execSQL("INSERT INTO user_quest_cross_ref VALUES (1, 3, $now)")
            db.execSQL("INSERT INTO user_quest_cross_ref VALUES (2, 6, $now)")
        }
    }
}
