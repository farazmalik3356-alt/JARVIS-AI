package com.jarvis.ai.action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AssistantAction {

    abstract val requiresConfirmation: Boolean

    @Serializable
    @SerialName("open_app")
    data class OpenApp(
        @SerialName("appName") val appName: String,
        override val requiresConfirmation: Boolean = false
    ) : AssistantAction()

    @Serializable
    @SerialName("open_settings")
    data class OpenSettings(
        @SerialName("target") val target: String,
        override val requiresConfirmation: Boolean = false
    ) : AssistantAction()

    @Serializable
    @SerialName("create_reminder")
    data class CreateReminder(
        @SerialName("title") val title: String,
        @SerialName("isoTime") val isoTime: String,
        @SerialName("notes") val notes: String? = null,
        override val requiresConfirmation: Boolean = true
    ) : AssistantAction()

    @Serializable
    @SerialName("share_text")
    data class ShareText(
        @SerialName("text") val text: String,
        override val requiresConfirmation: Boolean = true
    ) : AssistantAction()

    @Serializable
    @SerialName("open_url")
    data class OpenUrl(
        @SerialName("url") val url: String,
        override val requiresConfirmation: Boolean = false
    ) : AssistantAction()

    @Serializable
    @SerialName("speak")
    data class Speak(
        @SerialName("text") val text: String,
        override val requiresConfirmation: Boolean = false
    ) : AssistantAction()

    @Serializable
    @SerialName("unknown")
    data class UnknownAction(
        @SerialName("raw") val raw: String,
        override val requiresConfirmation: Boolean = false
    ) : AssistantAction()

    fun describe(): String = when (this) {
        is OpenApp -> "Open app: $appName"
        is OpenSettings -> "Open settings: $target"
        is CreateReminder -> "Reminder: $title @ $isoTime"
        is ShareText -> "Share text (${text.length} chars)"
        is OpenUrl -> "Open link: $url"
        is Speak -> "Speak aloud"
        is UnknownAction -> "Unrecognized action"
    }
}