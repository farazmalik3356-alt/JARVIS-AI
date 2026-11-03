package com.jarvis.ai.action

import com.jarvis.ai.action.handlers.AppLauncher
import com.jarvis.ai.action.handlers.SettingsLauncher
import com.jarvis.ai.action.handlers.ShareIntentHandler
import com.jarvis.ai.action.handlers.UrlOpener
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActionExecutor @Inject constructor(
    private val appLauncher: AppLauncher,
    private val settingsLauncher: SettingsLauncher,
    private val urlOpener: UrlOpener,
    private val shareIntentHandler: ShareIntentHandler,
    private val reminderScheduler: ReminderScheduler,
    private val planValidator: PlanValidator
) {

    fun execute(plan: CommandPlan, confirmedIndices: Set<Int> = emptySet()): Flow<StepResult> = flow {
        for (step in plan.actions) {
            val validation = planValidator.validate(step)
            if (validation is ValidationResult.Invalid) {
                emit(StepResult(step.index, step.action,
                    StepOutcome.Failure(validation.reason)))
                continue
            }
            if (step.action.requiresConfirmation && step.index !in confirmedIndices) {
                emit(StepResult(step.index, step.action, StepOutcome.AwaitingConfirmation))
                continue
            }
            val result = try {
                withTimeoutOrNull(STEP_TIMEOUT_MS) { run(step.action) }
                    ?: StepOutcome.Failure("Timed out")
            } catch (ce: CancellationException) {
                throw ce
            } catch (e: Throwable) {
                StepOutcome.Failure(e.message ?: "Execution failed")
            }
            emit(StepResult(step.index, step.action, result))
        }
    }

    private suspend fun run(action: AssistantAction): StepOutcome = when (action) {
        is AssistantAction.OpenApp -> appLauncher.open(action.appName)
        is AssistantAction.OpenSettings -> settingsLauncher.open(action.target)
        is AssistantAction.OpenUrl -> urlOpener.open(action.url)
        is AssistantAction.ShareText -> shareIntentHandler.share(action.text)
        is AssistantAction.Speak -> StepOutcome.Success("Speak queued.") // handled by TTS via UI
        is AssistantAction.CreateReminder -> {
            val r = reminderScheduler.create(action.title, action.isoTime, action.notes)
            r.fold(
                onSuccess = { StepOutcome.Success("Reminder set: ${it.title}") },
                onFailure = { StepOutcome.Failure(it.message ?: "Could not create reminder") }
            )
        }
        is AssistantAction.UnknownAction -> StepOutcome.Skipped("Unrecognized action")
    }

    companion object {
        private const val STEP_TIMEOUT_MS = 15_000L
    }
}