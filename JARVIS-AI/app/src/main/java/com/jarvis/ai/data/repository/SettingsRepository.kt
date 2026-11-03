package com.jarvis.ai.data.repository

import com.jarvis.ai.data.local.prefs.UserPreferences
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val preferences: Flow<UserPreferences>
    suspend fun setOnboardingComplete(done: Boolean)
    suspend fun setVoiceOutputEnabled(enabled: Boolean)
    suspend fun setSpeechRate(rate: Float)
    suspend fun setSpeechLanguage(tag: String)
    suspend fun setPreferredLanguage(lang: String)
    suspend fun setLastConversationId(id: String?)
}