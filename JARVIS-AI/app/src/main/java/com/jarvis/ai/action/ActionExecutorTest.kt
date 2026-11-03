package com.jarvis.ai.action

import com.google.common.truth.Truth.assertThat
import com.jarvis.ai.action.handlers.AppLauncher
import com.jarvis.ai.action.handlers.SettingsLauncher
import com.jarvis.ai.action.handlers.ShareIntentHandler
import com.jarvis.ai.action.handlers.UrlOpener
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ActionExecutorTest {

    private val appLauncher: AppLauncher = mockk()
    private val settingsLauncher: SettingsLauncher = mockk()
    private val urlOpener: UrlOpener = mockk()
    private val shareHandler: ShareIntentHandler = mockk()
    private val reminderScheduler: ReminderScheduler = mockk()
    private val validator = PlanValidator()

    private val executor = ActionExecutor(
        appLauncher = appLauncher,
        settingsLauncher = settingsLauncher,
        urlOpener = urlOpener,
        shareIntentHandler = shareHandler,
        reminderScheduler = reminderScheduler,
        planValidator = validator
    )

    @Test
    fun openApp_succeeds_and_emitsSuccess() = runTest {
        every { appLauncher.open("YouTube") } returns StepOutcome.Success("Opened YouTube")
        val plan = CommandPlan(
            listOf(PlannedStep(0, AssistantAction.OpenApp("YouTube")))
        )
        val results = executor.execute(plan).toList()
        assertThat(results).hasSize(1)
        assertThat(results[0].outcome).isInstanceOf(StepOutcome.Success::class.java)
    }

    @Test
    fun requiresConfirmation_whenNotConfirmed_isAwaiting() = runTest {
        val plan = CommandPlan(
            listOf(PlannedStep(0, AssistantAction.CreateReminder(
                "Call", "2030-01-01T10:00:00Z"
            )))
        )
        val results = executor.execute(plan, confirmedIndices = emptySet()).toList()
        assertThat(results.single().outcome)
            .isInstanceOf(StepOutcome.AwaitingConfirmation::class.java)
    }

    @Test
    fun requiresConfirmation_whenConfirmed_executes() = runTest {
        coEvery {
            reminderScheduler.create("Call", "2030-01-01T10:00:00Z", null)
        } returns Result.failure(IllegalArgumentException("stub"))
        val plan = CommandPlan(
            listOf(PlannedStep(0, AssistantAction.CreateReminder(
                "Call", "2030-01-01T10:00:00Z"
            )))
        )
        val results = executor.execute(plan, confirmedIndices = setOf(0)).toList()
        assertThat(results.single().outcome)
            .isInstanceOf(StepOutcome.Failure::class.java)
    }

    @Test
    fun invalidAction_failsWithoutCallingHandler() = runTest {
        val plan = CommandPlan(
            listOf(PlannedStep(0, AssistantAction.OpenApp("")))
        )
        val results = executor.execute(plan).toList()
        assertThat(results.single().outcome)
            .isInstanceOf(StepOutcome.Failure::class.java)
    }
}