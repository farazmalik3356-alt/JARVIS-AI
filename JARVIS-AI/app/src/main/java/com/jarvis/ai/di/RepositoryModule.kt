package com.jarvis.ai.di

import com.jarvis.ai.data.repository.ConversationRepository
import com.jarvis.ai.data.repository.ConversationRepositoryImpl
import com.jarvis.ai.data.repository.GeminiRepository
import com.jarvis.ai.data.repository.GeminiRepositoryImpl
import com.jarvis.ai.data.repository.ReminderRepository
import com.jarvis.ai.data.repository.ReminderRepositoryImpl
import com.jarvis.ai.data.repository.SettingsRepository
import com.jarvis.ai.data.repository.SettingsRepositoryImpl
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
    abstract fun bindGeminiRepository(impl: GeminiRepositoryImpl): GeminiRepository

    @Binds
    @Singleton
    abstract fun bindConversationRepository(impl: ConversationRepositoryImpl): ConversationRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindReminderRepository(impl: ReminderRepositoryImpl): ReminderRepository
}