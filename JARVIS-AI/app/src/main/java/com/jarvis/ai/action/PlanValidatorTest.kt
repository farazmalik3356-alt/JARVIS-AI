package com.jarvis.ai.action

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PlanValidatorTest {

    private val validator = PlanValidator()

    private fun step(action: AssistantAction) = PlannedStep(index = 0, action = action)

    @Test
    fun openApp_valid() {
        assertThat(validator.validate(step(AssistantAction.OpenApp("YouTube"))))
            .isEqualTo(ValidationResult.Valid)
    }

    @Test
    fun openApp_blankIsInvalid() {
        assertThat(validator.validate(step(AssistantAction.OpenApp(" "))))
            .isInstanceOf(ValidationResult.Invalid::class.java)
    }

    @Test
    fun reminder_invalidTime() {
        val s = step(AssistantAction.CreateReminder("x", "not-a-time"))
        assertThat(validator.validate(s)).isInstanceOf(ValidationResult.Invalid::class.java)
    }

    @Test
    fun reminder_validTime() {
        val s = step(AssistantAction.CreateReminder("x", "2030-01-01T10:00:00Z"))
        assertThat(validator.validate(s)).isEqualTo(ValidationResult.Valid)
    }

    @Test
    fun url_nonHttpIsInvalid() {
        val s = step(AssistantAction.OpenUrl("file:///etc/passwd"))
        assertThat(validator.validate(s)).isInstanceOf(ValidationResult.Invalid::class.java)
    }

    @Test
    fun unknownActionIsInvalid() {
        val s = step(AssistantAction.UnknownAction("rm -rf"))
        assertThat(validator.validate(s)).isInstanceOf(ValidationResult.Invalid::class.java)
    }

    @Test
    fun blankShareIsInvalid() {
        val s = step(AssistantAction.ShareText(""))
        assertThat(validator.validate(s)).isInstanceOf(ValidationResult.Invalid::class.java)
    }
}