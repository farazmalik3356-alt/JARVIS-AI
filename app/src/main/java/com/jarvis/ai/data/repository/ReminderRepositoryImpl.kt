package com.jarvis.ai.data.repository

import com.jarvis.ai.data.local.db.dao.ReminderDao
import com.jarvis.ai.data.local.db.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val dao: ReminderDao
) : ReminderRepository {

    override fun observeAll(): Flow<List<ReminderEntity>> = dao.observeAll()
    override suspend fun upsert(entity: ReminderEntity) = dao.upsert(entity)
    override suspend fun pending(now: Long): List<ReminderEntity> = dao.pending(now)
    override suspend fun markFired(id: String) = dao.markFired(id)
    override suspend fun delete(id: String) = dao.delete(id)
    override suspend fun clearAll() = dao.clearAll()
}