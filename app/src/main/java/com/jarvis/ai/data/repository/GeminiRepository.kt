package com.jarvis.ai.data.repository

import com.jarvis.ai.core.JarvisError
import kotlinx.coroutines.flow.Flow

data class ChatTurn(
    val role: String,   // "user" | "model"
    val text: String
)

interface GeminiRepository {

    /** Non-streaming single-shot generation with conversation context. */
    suspend fun generate(
        systemPrompt: String,
        history: List<ChatTurn>,
        userMessage: String
    ): String

    /** Lightweight key + connectivity probe. */
    suspend fun verifyApiKey(): Result<Unit>

    /** Streaming tokens if we later enable it; currently emits full text once. */
    fun generateStream(
        systemPrompt: String,
        history: List<ChatTurn>,
        userMessage: String
    ): Flow<StreamEvent>

    sealed class StreamEvent {
        data class Token(val text: String) : StreamEvent()
        data class Failed(val error: JarvisError) : StreamEvent()
        data object Done : StreamEvent()
    }
}