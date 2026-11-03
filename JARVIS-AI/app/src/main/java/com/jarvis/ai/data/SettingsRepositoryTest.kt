package com.jarvis.ai.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.google.common.truth.Truth.assertThat
import com.jarvis.ai.data.repository.SettingsRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SettingsRepositoryTest {

    /** In-memory DataStore fake. */
    private class FakeDataStore(initial: Preferences = emptyPreferences()) : DataStore<Preferences> {
        private val state = MutableStateFlow(initial)
        override val data: Flow<Preferences> = state
        override suspend fun updateData(
            transform: suspend (t: Preferences) -> Preferences
        ): Preferences {
            var out = emptyPreferences()
            state.update { current ->
                kotlinx.coroutines.runBlocking {
                    out = transform(current)
                }
                out
            }
            return out
        }
    }

    @Test
    fun defaults_areSensible() = runTest {
        val repo = SettingsRepositoryImpl(FakeDataStore())
        val prefs = repo.preferences.first()
        assertThat(prefs.onboardingComplete).isFalse()
        assertThat(prefs.voiceOutputEnabled).isTrue()
        assertThat(prefs.speechRate).isWithin(0.001f).of(1.0f)
        assertThat(prefs.speechLanguage).isEqualTo("en-US")
        assertThat(prefs.preferredLanguage).isEqualTo("auto")
    }

    @Test
    fun writing_preferencesRoundTrips() = runTest {
        val repo = SettingsRepositoryImpl(FakeDataStore())
        repo.setOnboardingComplete(true)
        repo.setVoiceOutputEnabled(false)
        repo.setSpeechRate(1.25f)
        repo.setSpeechLanguage("ur-PK")
        repo.setPreferredLanguage("roman_ur")
        repo.setLastConversationId("abc")

        val p = repo.preferences.first()
        assertThat(p.onboardingComplete).isTrue()
        assertThat(p.voiceOutputEnabled).isFalse()
        assertThat(p.speechRate).isWithin(0.001f).of(1.25f)
        assertThat(p.speechLanguage).isEqualTo("ur-PK")
        assertThat(p.preferredLanguage).isEqualTo("roman_ur")
        assertThat(p.lastConversationId).isEqualTo("abc")
    }

    @Test
    fun speechRate_isClamped() = runTest {
        val repo = SettingsRepositoryImpl(FakeDataStore())
        repo.setSpeechRate(5.0f)
        assertThat(repo.preferences.first().speechRate).isAtMost(2.0f)
        repo.setSpeechRate(0.1f)
        assertThat(repo.preferences.first().speechRate).isAtLeast(0.5f)
    }
}