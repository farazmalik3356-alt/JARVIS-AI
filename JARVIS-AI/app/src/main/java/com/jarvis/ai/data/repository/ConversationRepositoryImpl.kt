package com.jarvis.ai.data.repository

import com.jarvis.ai.data.local.db.dao.ConversationDao
import com.jarvis.ai.data.local.db.dao.MessageDao
import com.jarvis.ai.data.local.db.entity.ConversationEntity
import com.jarvis.ai.data.local.db.entity.MessageEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao
) : ConversationRepository {

    override fun observeConversations(): Flow<List<ConversationEntity>> =
        conversationDao.observeAll()

    override fun observeMessages(conversationId: String): Flow<List<MessageEntity>> =
        messageDao.observeForConversation(conversationId)

    override suspend fun createConversation(id: String, title: String): ConversationEntity {
        val now = System.currentTimeMillis()
        val entity = ConversationEntity(
            id = id,
            title = title.take(60).ifBlank { "New conversation" },
            createdAt = now,
            updatedAt = now
        )
        conversationDao.upsert(entity)
        return entity
    }

    override suspend fun upsertConversation(entity: ConversationEntity) {
        conversationDao.upsert(entity)
    }

    override suspend fun getConversation(id: String): ConversationEntity? =
        conversationDao.getById(id)

    override suspend fun insertMessage(entity: MessageEntity) {
        messageDao.insert(entity)
        conversationDao.getById(entity.conversationId)?.let { conv ->
            val preview = entity.content.trim().take(60).ifBlank { conv.title }
            conversationDao.touch(conv.id, System.currentTimeMillis(), preview)
        }
    }

    override suspend fun recentMessages(conversationId: String, limit: Int): List<MessageEntity> =
        messageDao.recentForConversation(conversationId, limit)

    override suspend fun deleteConversation(id: String) {
        messageDao.deleteForConversation(id)
        conversationDao.delete(id)
    }

    override suspend fun clearAll() {
        messageDao.clearAll()
        conversationDao.clearAll()
    }
}