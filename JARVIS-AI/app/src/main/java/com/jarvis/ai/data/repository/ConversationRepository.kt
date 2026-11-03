package com.jarvis.ai.data.repository

import com.jarvis.ai.data.local.db.entity.ConversationEntity
import com.jarvis.ai.data.local.db.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun observeConversations(): Flow<List<ConversationEntity>>
    fun observeMessages(conversationId: String): Flow<List<MessageEntity>>
    suspend fun createConversation(id: String, title: String): ConversationEntity
    suspend fun upsertConversation(entity: ConversationEntity)
    suspend fun getConversation(id: String): ConversationEntity?
    suspend fun insertMessage(entity: MessageEntity)
    suspend fun recentMessages(conversationId: String, limit: Int): List<MessageEntity>
    suspend fun deleteConversation(id: String)
    suspend fun clearAll()
}