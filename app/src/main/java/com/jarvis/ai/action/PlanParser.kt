package com.jarvis.ai.action

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Parses the strict JSON emitted by the planner model into a [CommandPlan].
 * No code from the model is trusted; every field is validated and coerced
 * into our own typed [AssistantAction] hierarchy.
 */
@Singleton
class PlanParser @Inject constructor() {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    fun parse(raw: String): CommandPlan {
        val cleaned = stripFences(raw)
        if (cleaned.isBlank()) return CommandPlan(emptyList())
        val root: JsonObject = runCatching { json.parseToJsonElement(cleaned).jsonObject }
            .getOrElse { return CommandPlan(emptyList()) }
        val arr: JsonArray = root["actions"] as? JsonArray ?: return CommandPlan(emptyList())
        val list = mutableListOf<PlannedStep>()
        var i = 0
        for (el in arr) {
            val obj = el as? JsonObject ?: continue
            val type = obj["type"]?.jsonPrimitive?.contentOrNull ?: continue
            val params = obj["params"] as? JsonObject ?: JsonObject(emptyMap())
            val action = toAction(type, params) ?: continue
            list += PlannedStep(index = i++, action = action)
        }
        return CommandPlan(list)
    }

    private fun toAction(type: String, p: JsonObject): AssistantAction? {
        fun str(key: String): String? =
            p[key]?.jsonPrimitive?.contentOrNull?.takeIf { it.isNotBlank() }
        return when (type) {
            "open_app" -> str("appName")?.let { AssistantAction.OpenApp(it) }
            "open_settings" -> {
                val target = str("target") ?: "main"
                AssistantAction.OpenSettings(target.lowercase())
            }
            "create_reminder" -> {
                val title = str("title") ?: return null
                val iso = str("isoTime") ?: str("time") ?: return null
                AssistantAction.CreateReminder(
                    title = title,
                    isoTime = iso,
                    notes = str("notes")
                )
            }
            "share_text" -> str("text")?.let { AssistantAction.ShareText(it) }
            "open_url" -> str("url")?.let {
                if (it.startsWith("http://") || it.startsWith("https://"))
                    AssistantAction.OpenUrl(it)
                else null
            }
            "speak" -> str("text")?.let { AssistantAction.Speak(it) }
            else -> null
        }
    }

    private fun stripFences(s: String): String {
        var t = s.trim()
        if (t.startsWith("```")) {
            t = t.removePrefix("```json").removePrefix("```").trim()
            if (t.endsWith("```")) t = t.removeSuffix("```").trim()
        }
        val start = t.indexOf('{')
        val end = t.lastIndexOf('}')
        return if (start >= 0 && end > start) t.substring(start, end + 1) else t
    }
}