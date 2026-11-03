package com.jarvis.ai.ui.voice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.ai.chat.ChatOrchestrator
import com.jarvis.ai.chat.ChatSessionManager
import com.jarvis.ai.core.JarvisError
import com.jarvis.ai.data.repository.ChatTurn
import com.jarvis.ai.data.repository.ConversationRepository
import com.jarvis.ai.voice.VoiceCoordinator
import com.jarvis.ai.voice.VoiceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VoiceUiState(
    val conversationId: String? = null,
    val lastUserText: String = "",
    val lastReply: String = "",
    val errorBanner: String? = null,
    val isProcessing: Boolean = false,
    val isSpeechAvailable: Boolean = true
)

@HiltViewModel
class VoiceViewModel @Inject constructor(
    private val sessionManager: ChatSessionManager,
    private val orchestrator: ChatOrchestrator,
    private val conversationRepository: ConversationRepository,
    val voiceCoordinator: VoiceCoordinator
) : ViewModel() {

    private val _state = MutableStateFlow(VoiceUiState())
    val state: StateFlow<VoiceUiState> = _state.asStateFlow()

    val voiceState: StateFlow<VoiceState> = voiceCoordinator.state

    private var turnJob: Job? = null

    init {
        _state.update {
            it.copy(isSpeechAvailable = voiceCoordinator.isSpeechAvailable())
        }
        viewModelScope.launch {
            val id = sessionManager.ensureSession(null)
            _state.update { it.copy(conversationId = id) }
        }
    }

    fun startListening(languageTag: String) {
        voiceCoordinator.stopSpeaking()
        _state.update { it.copy(errorBanner = null) }
        voiceCoordinator.startListening(
            scope = viewModelScope,
            languageTag = languageTag
        ) { finalText ->
            _state.update { it.copy(lastUserText = finalText) }
            handleUserText(finalText)
        }
    }

    fun stopListening() {
        voiceCoordinator.stopListening()
    }

    fun stopSpeaking() {
        voiceCoordinator.stopSpeaking()
    }

    private fun handleUserText(text: String) {
        val convId = _state.value.conversationId ?: return
        if (text.isBlank()) return
        _state.update { it.copy(isProcessing = true, lastReply = "", errorBanner = null) }
        turnJob?.cancel()
        turnJob = viewModelScope.launch {
            val history = loadHistory(convId)
            try {
                orchestrator.runTurn(
                    conversationId = convId,
                    userText = text,
                    history = history,
                    autoExecuteSafe = true
                ).collect { event ->
                    when (event) {
                        is ChatOrchestrator.TurnEvent.ReplyToken -> {
                            _state.update { it.copy(lastReply = event.text) }
                        }
                        is ChatOrchestrator.TurnEvent.ReplyPersisted -> {
                            voiceCoordinator.speak(viewModelScope, event.msg.content)
                        }
                        is ChatOrchestrator.TurnEvent.Completed -> {
                            _state.update { it.copy(isProcessing = false) }
                        }
                        is ChatOrchestrator.TurnEvent.Failed -> {
                            _state.update {
                                it.copy(
                                    isProcessing = false,
                                    errorBanner = event.error.userMessage()
                                )
                            }
                        }
                        else -> Unit
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isProcessing = false,
                        errorBanner = (e as? JarvisError)?.userMessage()
                            ?: "Something went wrong."
                    )
                }
            }
        }
    }

    private suspend fun loadHistory(convId: String): List<ChatTurn> {
        return conversationRepository.recentMessages(convId, 20)
            .filter { it.role == "user" || it.role == "model" }
            .map { ChatTurn(role = it.role, text = it.content) }
    }

    fun clearError() {
        _state.update { it.copy(errorBanner = null) }
    }

    override fun onCleared() {
        super.onCleared()
        turnJob?.cancel()
        voiceCoordinator.stopListening()
        voiceCoordinator.stopSpeaking()
    }
}