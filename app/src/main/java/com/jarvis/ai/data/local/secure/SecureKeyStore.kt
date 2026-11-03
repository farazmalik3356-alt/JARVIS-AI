package com.jarvis.ai.data.local.secure

interface SecureKeyStore {
    fun saveApiKey(key: String)
    fun readApiKey(): String?
    fun clearApiKey()
    fun hasApiKey(): Boolean
}