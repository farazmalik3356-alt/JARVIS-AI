package com.jarvis.ai.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jarvis.ai.action.CommandPlan
import com.jarvis.ai.action.StepOutcome
import com.jarvis.ai.action.StepResult
import com.jarvis.ai.ui.components.GlassCard
import com.jarvis.ai.ui.components.PrimaryButton
import com.jarvis.ai.ui.components.SecondaryButton
import com.jarvis.ai.ui.theme.ElectricViolet
import com.jarvis.ai.ui.theme.ErrorRed
import com.jarvis.ai.ui.theme.NeonCyan
import com.jarvis.ai.ui.theme.Space
import com.jarvis.ai.ui.theme.SuccessGreen
import com.jarvis.ai.ui.theme.TextPrimary
import com.jarvis.ai.ui.theme.TextSecondary

@Composable
fun PlanConfirmationCard(
    plan: CommandPlan?,
    results: List<StepResult>,
    executing: Boolean,
    onConfirm: () -> Unit,
    onReject: () -> Unit
) {
    if (plan == null) return
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Space.md, vertical = Space.sm),
        glowCyan = true
    ) {
        Column(modifier = Modifier.padding(Space.lg)) {
            Text(
                text = "Action plan",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Text(
                text = "${plan.actions.size} step(s) detected",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Spacer(Modifier.size(Space.sm))
            plan.actions.forEach { step ->
                val outcome = results.firstOrNull { it.index == step.index }?.outcome
                val (label, color) = when (outcome) {
                    is StepOutcome.Success -> "OK" to SuccessGreen
                    is StepOutcome.Failure -> "FAIL" to ErrorRed
                    is StepOutcome.Skipped -> "SKIP" to TextSecondary
                    is StepOutcome.AwaitingConfirmation -> "CONFIRM" to ElectricViolet
                    null -> "PENDING" to TextSecondary
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = color,
                        modifier = Modifier.width(70.dp)
                    )
                    Text(
                        text = step.action.describe(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                }
            }
            Spacer(Modifier.size(Space.md))
            Row(horizontalArrangement = Arrangement.spacedBy(Space.sm)) {
                SecondaryButton(
                    text = "Dismiss",
                    onClick = onReject,
                    modifier = Modifier.weight(1f)
                )
                PrimaryButton(
                    text = if (executing) "Executing…" else "Confirm",
                    onClick = onConfirm,
                    enabled = !executing,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}