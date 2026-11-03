package com.jarvis.ai.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jarvis.ai.data.local.db.dao.ConversationDao
import com.jarvis.ai.data.local.db.dao.MessageDao
import com.jarvis.ai.data.local.db.dao.ReminderDao
import com.jarvis.ai.data.local.db.entity.ConversationEntity
import com.jarvis.ai.data.local.db.entity.MessageEntity
import com.jarvis.ai.data.local.db.entity.ReminderEntity

@Database(
    entities = [
        ConversationEntity::class,
        MessageEntity::class,
        ReminderEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class JarvisDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun reminderDao(): ReminderDao
}