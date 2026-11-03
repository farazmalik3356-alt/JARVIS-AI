package com.jarvis.ai.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val triggerAtMillis: Long,
    val createdAt: Long,
    val fired: Boolean = false
)