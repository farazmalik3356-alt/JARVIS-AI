package com.jarvis.ai.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.ai.data.local.secure.SecureKeyStore
import com.jarvis.ai.data.repository.GeminiRepository
import com.jarvis.ai.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ApiSetupUiState(
    val keyInput: String = "",
    val isSaving: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val secureKeyStore: SecureKeyStore,
    private val geminiRepository: GeminiRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ApiSetupUiState())
    val state: StateFlow<ApiSetupUiState> = _state.asStateFlow()

    fun onKeyChange(text: String) {
        _state.value = _state.value.copy(keyInput = text, error = null)
    }

    fun saveAndVerify() {
        val key = _state.value.keyInput.trim()
        if (key.isBlank()) {
            _state.value = _state.value.copy(error = "Please paste your API key.")
            return
        }
        _state.value = _state.value.copy(isSaving = true, error = null)
        viewModelScope.launch {
            try {
                secureKeyStore.saveApiKey(key)
                val result = geminiRepository.verifyApiKey()
                result.fold(
                    onSuccess = {
                        _state.value = _state.value.copy(
                            isSaving = false,
                            success = true
                        )
                    },
                    onFailure = { e ->
                        secureKeyStore.clearApiKey()
                        _state.value = _state.value.copy(
                            isSaving = false,
                            error = (e as? com.jarvis.ai.core.JarvisError)?.userMessage()
                                ?: (e.message ?: "Verification failed.")
                        )
                    }
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    error = e.message ?: "Could not save key."
                )
            }
        }
    }

    fun markOnboardingComplete() = viewModelScope.launch {
        settingsRepository.setOnboardingComplete(true)
    }
}