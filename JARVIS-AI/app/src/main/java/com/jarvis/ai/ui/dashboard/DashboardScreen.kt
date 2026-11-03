package com.jarvis.ai.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jarvis.ai.ui.components.AiOrb
import com.jarvis.ai.ui.components.GlassCard
import com.jarvis.ai.ui.components.GradientBackground
import com.jarvis.ai.ui.theme.BorderSubtle
import com.jarvis.ai.ui.theme.ElectricViolet
import com.jarvis.ai.ui.theme.NeonCyan
import com.jarvis.ai.ui.theme.Space
import com.jarvis.ai.ui.theme.TextPrimary
import com.jarvis.ai.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    onOpenChat: () -> Unit,
    onOpenVoice: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val hasKey by viewModel.hasApiKey.collectAsStateWithLifecycle()
    val reminders by viewModel.reminders.collectAsStateWithLifecycle()

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(Space.xl),
            verticalArrangement = Arrangement.spacedBy(Space.lg)
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Good ${greeting()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Text(
                        text = "I'm listening, Sir.",
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextPrimary
                    )
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0x14FFFFFF))
                        .clickable(onClick = onOpenSettings),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = NeonCyan
                    )
                }
            }

            // Central orb
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Space.xl),
                contentAlignment = Alignment.Center
            ) {
                AiOrb(size = 220.dp)
            }

            if (!hasKey) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    glowCyan = true
                ) {
                    Column(modifier = Modifier.padding(Space.lg)) {
                        Text(
                            text = "Configure Gemini",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Spacer(Modifier.height(Space.xs))
                        Text(
                            text = "Add an API key in Settings to enable conversations and command planning.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Spacer(Modifier.height(Space.md))
                        Text(
                            text = "Open Settings →",
                            style = MaterialTheme.typography.labelLarge,
                            color = NeonCyan,
                            modifier = Modifier.clickable(onClick = onOpenSettings)
                        )
                    }
                }
            }

            // Action tiles
            Row(horizontalArrangement = Arrangement.spacedBy(Space.md)) {
                ActionTile(
                    icon = Icons.AutoMirrored.Filled.Chat,
                    label = "Chat",
                    onClick = onOpenChat,
                    modifier = Modifier.weight(1f)
                )
                ActionTile(
                    icon = Icons.Filled.Mic,
                    label = "Voice",
                    onClick = onOpenVoice,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Space.md)) {
                ActionTile(
                    icon = Icons.Filled.History,
                    label = "History",
                    onClick = onOpenHistory,
                    modifier = Modifier.weight(1f)
                )
                ActionTile(
                    icon = Icons.Filled.Alarm,
                    label = "Reminders",
                    onClick = onOpenSettings,
                    modifier = Modifier.weight(1f)
                )
            }

            if (reminders.isNotEmpty()) {
                Text(
                    text = "Upcoming reminders",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = Space.lg)
                )
                reminders.take(3).forEach { r ->
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(Space.md),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Alarm,
                                contentDescription = null,
                                tint = ElectricViolet,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.size(Space.md))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = r.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary
                                )
                                Text(
                                    text = SimpleDateFormat(
                                        "MMM d, HH:mm", Locale.getDefault()
                                    ).format(Date(r.triggerAtMillis)),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(Space.xxl))
        }
    }
}

@Composable
private fun ActionTile(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        cornerRadiusDp = 18
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(Space.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                NeonCyan.copy(alpha = 0.25f),
                                ElectricViolet.copy(alpha = 0.25f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = label, tint = NeonCyan)
            }
            Spacer(Modifier.height(Space.md))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun greeting(): String {
    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> "morning"
        in 12..16 -> "afternoon"
        in 17..21 -> "evening"
        else -> "night"
    }
}