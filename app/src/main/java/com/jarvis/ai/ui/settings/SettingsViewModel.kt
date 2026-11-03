package com.jarvis.ai.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.ai.data.local.secure.SecureKeyStore
import com.jarvis.ai.data.repository.ConversationRepository
import com.jarvis.ai.data.repository.GeminiRepository
import com.jarvis.ai.data.repository.ReminderRepository
import com.jarvis.ai.data.repository.SettingsRepository
import com.jarvis.ai.voice.VoiceCoordinator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val hasApiKey: Boolean = false,
    val apiKeyInput: String = "",
    val apiError: String? = null,
    val verifying: Boolean = false,
    val apiVerified: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val secureKeyStore: SecureKeyStore,
    private val geminiRepository: GeminiRepository,
    private val settingsRepository: SettingsRepository,
    private val conversationRepository: ConversationRepository,
    private val reminderRepository: ReminderRepository,
    private val voiceCoordinator: VoiceCoordinator
) : ViewModel() {

    val preferences = settingsRepository.preferences
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), com.jarvis.ai.data.local.prefs.UserPreferences())

    private val _state = MutableStateFlow(
        SettingsUiState(hasApiKey = secureKeyStore.hasApiKey())
    )
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    fun onApiKeyChange(text: String) {
        _state.update { it.copy(apiKeyInput = text, apiError = null, apiVerified = false) }
    }

    fun saveApiKey() {
        val key = _state.value.apiKeyInput.trim()
        if (key.isBlank()) {
            _state.update { it.copy(apiError = "Enter a key.") }
            return
        }
        _state.update { it.copy(verifying = true, apiError = null) }
        viewModelScope.launch {
            secureKeyStore.saveApiKey(key)
            geminiRepository.verifyApiKey().fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            verifying = false,
                            hasApiKey = true,
                            apiKeyInput = "",
                            apiVerified = true
                        )
                    }
                },
                onFailure = { e ->
                    secureKeyStore.clearApiKey()
                    _state.update {
                        it.copy(
                            verifying = false,
                            hasApiKey = false,
                            apiError = (e as? com.jarvis.ai.core.JarvisError)?.userMessage()
                                ?: "Verification failed."
                        )
                    }
                }
            )
        }
    }

    fun removeApiKey() = viewModelScope.launch {
        secureKeyStore.clearApiKey()
        _state.update { it.copy(hasApiKey = false, apiVerified = false) }
    }

    fun setVoiceOutput(enabled: Boolean) = viewModelScope.launch {
        settingsRepository.setVoiceOutputEnabled(enabled)
    }

    fun setSpeechRate(rate: Float) = viewModelScope.launch {
        settingsRepository.setSpeechRate(rate)
        voiceCoordinator.stopSpeaking()
    }

    fun setSpeechLanguage(tag: String) = viewModelScope.launch {
        settingsRepository.setSpeechLanguage(tag)
    }

    fun setPreferredLanguage(lang: String) = viewModelScope.launch {
        settingsRepository.setPreferredLanguage(lang)
    }

    fun eraseAllLocalData() = viewModelScope.launch {
        conversationRepository.clearAll()
        reminderRepository.clearAll()
        secureKeyStore.clearApiKey()
        _state.update { it.copy(hasApiKey = false) }
    }
}