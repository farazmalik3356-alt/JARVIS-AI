package com.jarvis.ai.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jarvis.ai.action.CommandPlan
import com.jarvis.ai.action.StepOutcome
import com.jarvis.ai.action.StepResult
import com.jarvis.ai.chat.ChatOrchestrator
import com.jarvis.ai.chat.ChatSessionManager
import com.jarvis.ai.core.JarvisError
import com.jarvis.ai.data.local.db.entity.MessageEntity
import com.jarvis.ai.data.repository.ChatTurn
import com.jarvis.ai.data.repository.ConversationRepository
import com.jarvis.ai.voice.VoiceCoordinator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class ChatUiState(
    val conversationId: String? = null,
    val messages: List<MessageEntity> = emptyList(),
    val inputText: String = "",
    val isSending: Boolean = false,
    val errorBanner: String? = null,
    val pendingPlan: CommandPlan? = null,
    val pendingPlanResults: List<StepResult> = emptyList(),
    val isExecutingPlan: Boolean = false,
    val lastPlanSummary: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sessionManager: ChatSessionManager,
    private val orchestrator: ChatOrchestrator,
    private val conversationRepository: ConversationRepository,
    private val voiceCoordinator: VoiceCoordinator
) : ViewModel() {

    private val _state = MutableStateFlow(ChatUiState())
    val state: StateFlow<ChatUiState> = _state.asStateFlow()

    private var activeJob: Job? = null
    private var observeJob: Job? = null

    init {
        viewModelScope.launch {
            val id = sessionManager.ensureSession(null)
            _state.update { it.copy(conversationId = id) }
            observeMessages(id)
        }
    }

    private fun observeMessages(conversationId: String) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            conversationRepository.observeMessages(conversationId).collect { list ->
                _state.update { it.copy(messages = list) }
            }
        }
    }

    fun onInputChange(text: String) {
        _state.update { it.copy(inputText = text) }
    }

    fun sendMessage(speakReply: Boolean = false) {
        val text = _state.value.inputText.trim()
        if (text.isEmpty()) return
        val convId = _state.value.conversationId ?: return
        if (_state.value.isSending) return

        _state.update {
            it.copy(
                inputText = "",
                isSending = true,
                errorBanner = null,
                lastPlanSummary = null
            )
        }

        val history = buildHistory()

        activeJob = viewModelScope.launch {
            try {
                orchestrator.runTurn(
                    conversationId = convId,
                    userText = text,
                    history = history,
                    autoExecuteSafe = true
                ).collect { event ->
                    when (event) {
                        is ChatOrchestrator.TurnEvent.UserPersisted -> Unit
                        is ChatOrchestrator.TurnEvent.ReplyToken -> Unit
                        is ChatOrchestrator.TurnEvent.ReplyPersisted -> {
                            if (speakReply) {
                                voiceCoordinator.speak(viewModelScope, event.msg.content)
                            }
                        }
                        is ChatOrchestrator.TurnEvent.PlanReady -> {
                            _state.update { it.copy(pendingPlan = event.plan) }
                        }
                        is ChatOrchestrator.TurnEvent.StepProgress -> {
                            _state.update {
                                it.copy(pendingPlanResults = it.pendingPlanResults + event.result)
                            }
                        }
                        is ChatOrchestrator.TurnEvent.Completed -> {
                            _state.update {
                                it.copy(
                                    isSending = false,
                                    lastPlanSummary = event.result?.summaryLine()
                                )
                            }
                            if (event.result != null) {
                                val hasAwaiting = event.result.steps
                                    .any { s -> s.outcome is StepOutcome.AwaitingConfirmation }
                                if (!hasAwaiting) {
                                    _state.update {
                                        it.copy(
                                            pendingPlan = null,
                                            pendingPlanResults = emptyList()
                                        )
                                    }
                                }
                            }
                        }
                        is ChatOrchestrator.TurnEvent.Failed -> {
                            _state.update {
                                it.copy(
                                    isSending = false,
                                    errorBanner = event.error.userMessage()
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSending = false,
                        errorBanner = (e as? JarvisError)?.userMessage()
                            ?: (e.message ?: "Something went wrong.")
                    )
                }
            }
        }
    }

    fun cancelCurrentTurn() {
        activeJob?.cancel()
        activeJob = null
        _state.update { it.copy(isSending = false) }
    }

    fun confirmPlan() {
        val plan = _state.value.pendingPlan ?: return
        val convId = _state.value.conversationId ?: return
        val awaitingIndices = _state.value.pendingPlanResults
            .filter { it.outcome is StepOutcome.AwaitingConfirmation }
            .map { it.index }
            .toSet()
        if (awaitingIndices.isEmpty()) {
            _state.update { it.copy(pendingPlan = null, pendingPlanResults = emptyList()) }
            return
        }
        _state.update { it.copy(isExecutingPlan = true) }
        viewModelScope.launch {
            val finalResults = mutableListOf<StepResult>()
            orchestrator.executeConfirmed(plan, awaitingIndices).collect { r ->
                finalResults += r
            }
            val summary = finalResults.joinToString("\n") { r ->
                val prefix = when (r.outcome) {
                    is StepOutcome.Success -> "[OK]"
                    is StepOutcome.Failure -> "[FAIL]"
                    is StepOutcome.Skipped -> "[SKIP]"
                    else -> "[…]"
                }
                val detail = when (val o = r.outcome) {
                    is StepOutcome.Success -> o.message
                    is StepOutcome.Failure -> o.reason
                    is StepOutcome.Skipped -> o.reason
                    else -> ""
                }
                "$prefix ${r.action.describe()} — $detail"
            }
            conversationRepository.insertMessage(
                MessageEntity(
                    id = UUID.randomUUID().toString(),
                    conversationId = convId,
                    role = "system",
                    content = "Plan executed:\n$summary",
                    createdAt = System.currentTimeMillis()
                )
            )
            _state.update {
                it.copy(
                    isExecutingPlan = false,
                    pendingPlan = null,
                    pendingPlanResults = emptyList()
                )
            }
        }
    }

    fun rejectPlan() {
        _state.update {
            it.copy(pendingPlan = null, pendingPlanResults = emptyList())
        }
    }

    fun clearError() {
        _state.update { it.copy(errorBanner = null) }
    }

    fun startNewChat() {
        activeJob?.cancel()
        viewModelScope.launch {
            val id = sessionManager.startNew()
            _state.value = ChatUiState(conversationId = id)
            observeMessages(id)
        }
    }

    private fun buildHistory(): List<ChatTurn> {
        return _state.value.messages
            .filter { it.role == "user" || it.role == "model" }
            .takeLast(20)
            .map { ChatTurn(role = it.role, text = it.content) }
    }

    override fun onCleared() {
        super.onCleared()
        activeJob?.cancel()
        observeJob?.cancel()
    }
}