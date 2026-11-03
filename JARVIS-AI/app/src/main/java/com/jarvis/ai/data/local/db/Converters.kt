package com.jarvis.ai.data.local.db

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }

    @TypeConverter
    fun fromList(value: List<String>?): String =
        json.encodeToString(ListSerializerAny, value ?: emptyList())

    @TypeConverter
    fun toList(value: String?): List<String> =
        if (value.isNullOrBlank()) emptyList() else runCatching {
            json.decodeFromString(ListSerializerAny, value)
        }.getOrDefault(emptyList())

    companion object {
        private val ListSerializerAny =
            kotlinx.serialization.builtins.ListSerializer(kotlinx.serialization.builtins.serializer<String>())
    }
}