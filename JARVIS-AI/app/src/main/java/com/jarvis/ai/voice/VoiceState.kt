package com.jarvis.ai.voice

sealed class VoiceState {
    data object Idle : VoiceState()
    data object Listening : VoiceState()
    data class Processing(val partial: String) : VoiceState()
    data class Speaking(val text: String) : VoiceState()
    data class Error(val message: String) : VoiceState()
}