package com.jarvis.ai.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jarvis.ai.data.local.prefs.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    override val preferences: Flow<UserPreferences> = dataStore.data.map { p ->
        UserPreferences(
            onboardingComplete = p[K_ONBOARD] ?: false,
            voiceOutputEnabled = p[K_VOICE] ?: true,
            speechRate = p[K_RATE] ?: 1.0f,
            speechLanguage = p[K_LANG] ?: "en-US",
            preferredLanguage = p[K_PREF_LANG] ?: "auto",
            lastConversationId = p[K_LAST_CONV]
        )
    }

    override suspend fun setOnboardingComplete(done: Boolean) {
        dataStore.edit { it[K_ONBOARD] = done }
    }

    override suspend fun setVoiceOutputEnabled(enabled: Boolean) {
        dataStore.edit { it[K_VOICE] = enabled }
    }

    override suspend fun setSpeechRate(rate: Float) {
        dataStore.edit { it[K_RATE] = rate.coerceIn(0.5f, 2.0f) }
    }

    override suspend fun setSpeechLanguage(tag: String) {
        dataStore.edit { it[K_LANG] = tag }
    }

    override suspend fun setPreferredLanguage(lang: String) {
        dataStore.edit { it[K_PREF_LANG] = lang }
    }

    override suspend fun setLastConversationId(id: String?) {
        dataStore.edit { p ->
            if (id == null) p.remove(K_LAST_CONV) else p[K_LAST_CONV] = id
        }
    }

    companion object {
        private val K_ONBOARD = booleanPreferencesKey("onboarding_complete")
        private val K_VOICE = booleanPreferencesKey("voice_output_enabled")
        private val K_RATE = floatPreferencesKey("speech_rate")
        private val K_LANG = stringPreferencesKey("speech_language")
        private val K_PREF_LANG = stringPreferencesKey("preferred_language")
        private val K_LAST_CONV = stringPreferencesKey("last_conversation_id")
    }
}