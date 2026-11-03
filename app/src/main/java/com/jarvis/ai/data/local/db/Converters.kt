package com.jarvis.ai.data.local.db

import androidx.room.TypeConverter

/**
 * Room type converters for JARVIS AI.
 *
 * NOTE: Current entities (ConversationEntity, MessageEntity, ReminderEntity)
 * do not use List<String> fields, so these converters are kept simple and
 * dependency-free. If you later add a List<String> column, replace these
 * with a JSON-backed implementation.
 */
class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return value?.joinToString(separator = "\u001F") ?: ""
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        return value.split("\u001F")
    }
}
