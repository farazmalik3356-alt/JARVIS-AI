package com.jarvis.ai.di

import android.content.Context
import androidx.room.Room
import com.jarvis.ai.data.local.db.JarvisDatabase
import com.jarvis.ai.data.local.db.dao.ConversationDao
import com.jarvis.ai.data.local.db.dao.MessageDao
import com.jarvis.ai.data.local.db.dao.ReminderDao
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ): JarvisDatabase = Room.databaseBuilder(
        context,
        JarvisDatabase::class.java,
        "jarvis.db"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideConversationDao(db: JarvisDatabase): ConversationDao = db.conversationDao()

    @Provides
    fun provideMessageDao(db: JarvisDatabase): MessageDao = db.messageDao()

    @Provides
    fun provideReminderDao(db: JarvisDatabase): ReminderDao = db.reminderDao()
}