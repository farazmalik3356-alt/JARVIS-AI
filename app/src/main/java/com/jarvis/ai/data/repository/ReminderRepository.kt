package com.jarvis.ai.data.repository

import com.jarvis.ai.data.local.db.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun observeAll(): Flow<List<ReminderEntity>>
    suspend fun upsert(entity: ReminderEntity)
    suspend fun pending(now: Long): List<ReminderEntity>
    suspend fun markFired(id: String)
    suspend fun delete(id: String)
    suspend fun clearAll()
}