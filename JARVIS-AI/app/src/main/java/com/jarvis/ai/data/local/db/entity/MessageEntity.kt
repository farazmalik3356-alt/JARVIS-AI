package com.jarvis.ai.data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    indices = [Index("conversationId"), Index("createdAt")]
)
data class MessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val role: String,          // "user" | "model" | "system" | "error"
    val content: String,
    val createdAt: Long,
    val isError: Boolean = false,
    val planJson: String? = null
)