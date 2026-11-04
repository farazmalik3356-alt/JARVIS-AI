package com.jarvis.ai.ui.voice

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jarvis.ai.ui.components.AiOrb
import com.jarvis.ai.ui.components.GradientBackground
import com.jarvis.ai.ui.components.JarvisTopBar
import com.jarvis.ai.ui.theme.ElectricViolet
import com.jarvis.ai.ui.theme.ErrorRed
import com.jarvis.ai.ui.theme.NeonCyan
import com.jarvis.ai.ui.theme.Space
import com.jarvis.ai.ui.theme.TextPrimary
import com.jarvis.ai.ui.theme.TextSecondary
import com.jarvis.ai.voice.VoiceState

@Composable
fun VoiceScreen(
    onBack: () -> Unit,
    speechLanguage: String,
    speechRate: Float,
    viewModel: VoiceViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val voiceState by viewModel.voiceState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var hasMicPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasMicPermission = granted
        if (granted) viewModel.startListening(speechLanguage)
    }

    GradientBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            JarvisTopBar(
                title = "Voice",
                subtitle = "Speak naturally",
                onBack = onBack
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = Space.xl),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AiOrb(
                    size = 260.dp,
                    speaking = voiceState is VoiceState.Speaking,
                    listening = voiceState is VoiceState.Listening
                )
                Spacer(Modifier.height(Space.xl))
                Text(
                    text = voiceLabel(voiceState),
                    style = MaterialTheme.typography.titleMedium,
                    color = NeonCyan
                )
                Spacer(Modifier.height(Space.md))

                val vs = voiceState
                val (transcript, kind) = when (vs) {
                    is VoiceState.Processing -> vs.partial to "user"
                    is VoiceState.Speaking -> vs.text to "jarvis"
                    else -> uiState.lastReply.ifBlank { "" } to "jarvis"
                }
                if (transcript.isNotBlank()) {
                    Text(
                        text = if (kind == "user") "You: $transcript" else transcript,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = Space.md)
                    )
                } else if (uiState.lastUserText.isNotBlank()) {
                    Text(
                        text = "You: ${uiState.lastUserText}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }

                if (uiState.errorBanner != null) {
                    Spacer(Modifier.height(Space.md))
                    Text(
                        text = uiState.errorBanner.orEmpty(),
                        color = ErrorRed,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
                if (!uiState.isSpeechAvailable) {
                    Spacer(Modifier.height(Space.md))
                    Text(
                        text = "Speech recognition is not available on this device.",
                        color = ErrorRed,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Space.xl),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val listening = voiceState is VoiceState.Listening
                val speaking = voiceState is VoiceState.Speaking
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    if (listening) ErrorRed else NeonCyan,
                                    if (listening) ErrorRed else ElectricViolet
                                )
                            )
                        )
                        .clickable {
                            when {
                                speaking -> viewModel.stopSpeaking()
                                listening -> viewModel.stopListening()
                                uiState.isProcessing -> Unit
                                !hasMicPermission ->
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                else -> viewModel.startListening(speechLanguage)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when {
                            listening -> Icons.Filled.Stop
                            speaking -> Icons.Filled.Close
                            else -> Icons.Filled.Mic
                        },
                        contentDescription = "Mic",
                        tint = Color(0xFF0A0C10),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Spacer(Modifier.height(Space.xl))
        }
    }
}

private fun voiceLabel(state: VoiceState): String = when (state) {
    is VoiceState.Idle -> "Tap to speak"
    is VoiceState.Listening -> "Listening…"
    is VoiceState.Processing -> "Processing…"
    is VoiceState.Speaking -> "Speaking…"
    is VoiceState.Error -> state.message
}
