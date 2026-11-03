package com.jarvis.ai.action

data class CommandPlan(
    val actions: List<PlannedStep>
) {
    val isEmpty: Boolean get() = actions.isEmpty()
}

data class PlannedStep(
    val index: Int,
    val action: AssistantAction
)

sealed class StepOutcome {
    data class Success(val message: String) : StepOutcome()
    data class Failure(val reason: String) : StepOutcome()
    data class Skipped(val reason: String) : StepOutcome()
    data object AwaitingConfirmation : StepOutcome()
}

data class StepResult(
    val index: Int,
    val action: AssistantAction,
    val outcome: StepOutcome
)

data class PlanExecutionResult(
    val steps: List<StepResult>
) {
    val succeeded: Int get() = steps.count { it.outcome is StepOutcome.Success }
    val failed: Int get() = steps.count { it.outcome is StepOutcome.Failure }
    val skipped: Int get() = steps.count { it.outcome is StepOutcome.Skipped }

    fun summaryLine(): String = buildString {
        append("Completed $succeeded")
        if (failed > 0) append(" · Failed $failed")
        if (skipped > 0) append(" · Skipped $skipped")
    }
}