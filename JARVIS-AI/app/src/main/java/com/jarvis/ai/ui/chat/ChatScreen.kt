package com.jarvis.ai.ui.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jarvis.ai.data.local.db.entity.MessageEntity
import com.jarvis.ai.ui.components.GradientBackground
import com.jarvis.ai.ui.components.JarvisTopBar
import com.jarvis.ai.ui.theme.DeepNavy
import com.jarvis.ai.ui.theme.ElectricViolet
import com.jarvis.ai.ui.theme.ErrorRed
import com.jarvis.ai.ui.theme.NeonCyan
import com.jarvis.ai.ui.theme.Space
import com.jarvis.ai.ui.theme.TextPrimary
import com.jarvis.ai.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    onBack: () -> Unit,
    voiceEnabled: Boolean,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            JarvisTopBar(
                title = "JARVIS",
                subtitle = "Gemini-powered assistant",
                onBack = onBack,
                trailing = {
                    IconButton(onClick = viewModel::startNewChat) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "New chat",
                            tint = NeonCyan
                        )
                    }
                }
            )

            AnimatedVisibility(visible = state.errorBanner != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x22FF5C7A))
                        .padding(horizontal = Space.lg, vertical = Space.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.errorBanner.orEmpty(),
                        color = ErrorRed,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = viewModel::clearError) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "dismiss",
                            tint = ErrorRed,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    horizontal = Space.lg,
                    vertical = Space.md
                ),
                verticalArrangement = Arrangement.spacedBy(Space.sm)
            ) {
                if (state.messages.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(Modifier.size(Space.xxxl))
                            Text(
                                text = "No messages yet.",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextSecondary
                            )
                            Text(
                                text = "Ask anything, or issue a command.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
                items(
                    items = state.messages,
                    key = { it.id }
                ) { msg -> MessageBubble(msg) }
                if (state.isSending) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                color = NeonCyan,
                                strokeWidth = 1.5.dp
                            )
                            Spacer(Modifier.size(Space.sm))
                            Text(
                                text = "JARVIS is thinking…",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = state.pendingPlan != null) {
                PlanConfirmationCard(
                    plan = state.pendingPlan,
                    results = state.pendingPlanResults,
                    executing = state.isExecutingPlan,
                    onConfirm = viewModel::confirmPlan,
                    onReject = viewModel::rejectPlan
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Space.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = state.inputText,
                    onValueChange = viewModel::onInputChange,
                    placeholder = {
                        Text("Ask JARVIS…", color = TextSecondary)
                    },
                    modifier = Modifier.weight(1f),
                    maxLines = 5,
                    enabled = !state.isSending,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = { viewModel.sendMessage(speakReply = voiceEnabled) }
                    ),
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = DeepNavy,
                        unfocusedContainerColor = DeepNavy,
                        disabledContainerColor = DeepNavy,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                Spacer(Modifier.size(Space.sm))
                val enabled = state.inputText.isNotBlank() && !state.isSending
                IconButton(
                    onClick = {
                        if (state.isSending) viewModel.cancelCurrentTurn()
                        else viewModel.sendMessage(speakReply = voiceEnabled)
                    },
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            color = if (state.isSending) ErrorRed
                            else if (enabled) NeonCyan else Color(0xFF1F2733),
                            shape = RoundedCornerShape(26.dp)
                        )
                ) {
                    Icon(
                        imageVector = if (state.isSending) Icons.Filled.Stop
                        else Icons.AutoMirrored.Filled.Send,
                        contentDescription = if (state.isSending) "Stop" else "Send",
                        tint = if (enabled || state.isSending) Color(0xFF0A0C10)
                        else TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(msg: MessageEntity) {
    val isUser = msg.role == "user"
    val isSystem = msg.role == "system"
    val alignment = when {
        isUser -> Alignment.CenterEnd
        else -> Alignment.CenterStart
    }
    val bg = when {
        isUser -> NeonCyan.copy(alpha = 0.15f)
        isSystem -> ElectricViolet.copy(alpha = 0.15f)
        else -> Color(0x14FFFFFF)
    }
    val borderColor = when {
        isUser -> NeonCyan.copy(alpha = 0.35f)
        isSystem -> ElectricViolet.copy(alpha = 0.35f)
        else -> Color(0x1AFFFFFF)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .background(bg, RoundedCornerShape(16.dp))
                .padding(Space.md),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            if (!isUser && !isSystem) {
                Text(
                    text = "JARVIS",
                    style = MaterialTheme.typography.labelSmall,
                    color = NeonCyan
                )
                Spacer(Modifier.size(2.dp))
            }
            if (isSystem) {
                Text(
                    text = "SYSTEM",
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricViolet
                )
                Spacer(Modifier.size(2.dp))
            }
            Text(
                text = msg.content,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
            Spacer(Modifier.size(4.dp))
            Text(
                text = SimpleDateFormat("HH:mm", Locale.getDefault())
                    .format(Date(msg.createdAt)),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}