package com.jarvis.ai.action

import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import javax.inject.Inject
import javax.inject.Singleton

sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class Invalid(val reason: String) : ValidationResult()
}

@Singleton
class PlanValidator @Inject constructor() {

    fun validate(step: PlannedStep): ValidationResult = when (val a = step.action) {
        is AssistantAction.OpenApp ->
            if (a.appName.isBlank()) ValidationResult.Invalid("app name is blank")
            else ValidationResult.Valid

        is AssistantAction.OpenSettings ->
            if (a.target.isBlank()) ValidationResult.Invalid("settings target is blank")
            else ValidationResult.Valid

        is AssistantAction.CreateReminder -> {
            if (a.title.isBlank()) ValidationResult.Invalid("reminder title is blank")
            else {
                try {
                    OffsetDateTime.parse(a.isoTime)
                    ValidationResult.Valid
                } catch (e: DateTimeParseException) {
                    ValidationResult.Invalid("reminder time is not ISO8601: ${a.isoTime}")
                }
            }
        }

        is AssistantAction.ShareText ->
            if (a.text.isBlank()) ValidationResult.Invalid("share text is blank")
            else ValidationResult.Valid

        is AssistantAction.OpenUrl -> {
            val okScheme = a.url.startsWith("http://") || a.url.startsWith("https://")
            if (!okScheme) ValidationResult.Invalid("URL scheme not allowed")
            else ValidationResult.Valid
        }

        is AssistantAction.Speak ->
            if (a.text.isBlank()) ValidationResult.Invalid("speak text is blank")
            else ValidationResult.Valid

        is AssistantAction.UnknownAction ->
            ValidationResult.Invalid("unrecognized action")
    }

    fun validatePlan(plan: CommandPlan): List<ValidationResult> =
        plan.actions.map { validate(it) }
}