package com.jarvis.ai.data.local.prefs

data class UserPreferences(
    val onboardingComplete: Boolean = false,
    val voiceOutputEnabled: Boolean = true,
    val speechRate: Float = 1.0f,
    val speechLanguage: String = "en-US",
    val preferredLanguage: String = "auto",  // "auto" | "en" | "ur" | "roman_ur"
    val lastConversationId: String? = null
)