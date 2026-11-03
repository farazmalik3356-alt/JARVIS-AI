package com.jarvis.ai.chat

import com.jarvis.ai.data.local.db.entity.ConversationEntity
import com.jarvis.ai.data.repository.ConversationRepository
import com.jarvis.ai.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatSessionManager @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val settingsRepository: SettingsRepository
) {
    fun observeConversations(): Flow<List<ConversationEntity>> =
        conversationRepository.observeConversations()

    suspend fun ensureSession(existingId: String?): String {
        if (existingId != null && conversationRepository.getConversation(existingId) != null) {
            return existingId
        }
        val id = UUID.randomUUID().toString()
        conversationRepository.createConversation(id, "New conversation")
        settingsRepository.setLastConversationId(id)
        return id
    }

    suspend fun startNew(): String {
        val id = UUID.randomUUID().toString()
        conversationRepository.createConversation(id, "New conversation")
        settingsRepository.setLastConversationId(id)
        return id
    }
}