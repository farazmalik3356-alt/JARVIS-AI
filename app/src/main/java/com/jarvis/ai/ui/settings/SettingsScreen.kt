package com.jarvis.ai.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jarvis.ai.ui.components.GlassCard
import com.jarvis.ai.ui.components.GradientBackground
import com.jarvis.ai.ui.components.JarvisTopBar
import com.jarvis.ai.ui.components.PrimaryButton
import com.jarvis.ai.ui.theme.ErrorRed
import com.jarvis.ai.ui.theme.NeonCyan
import com.jarvis.ai.ui.theme.Space
import com.jarvis.ai.ui.theme.SuccessGreen
import com.jarvis.ai.ui.theme.TextPrimary
import com.jarvis.ai.ui.theme.TextSecondary
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val ui by viewModel.state.collectAsStateWithLifecycle()
    val prefs by viewModel.preferences.collectAsStateWithLifecycle()
    var showEraseDialog by remember { mutableStateOf(false) }

    GradientBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            JarvisTopBar(title = "Settings", onBack = onBack)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(Space.lg),
                verticalArrangement = Arrangement.spacedBy(Space.lg)
            ) {
                // API key
                GlassCard(modifier = Modifier.fillMaxWidth(), glowCyan = ui.apiVerified) {
                    Column(modifier = Modifier.padding(Space.lg)) {
                        Text(
                            text = "Gemini API Key",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Spacer(Modifier.height(Space.sm))
                        Text(
                            text = if (ui.hasApiKey) "Key configured · stored encrypted"
                            else "No key configured",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (ui.hasApiKey) SuccessGreen else TextSecondary
                        )
                        Spacer(Modifier.height(Space.md))
                        TextField(
                            value = ui.apiKeyInput,
                            onValueChange = viewModel::onApiKeyChange,
                            placeholder = { Text("Paste new key to replace") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                        if (ui.apiError != null) {
                            Spacer(Modifier.height(Space.xs))
                            Text(
                                text = ui.apiError!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = ErrorRed
                            )
                        }
                        Spacer(Modifier.height(Space.md))
                        PrimaryButton(
                            text = if (ui.verifying) "Verifying…" else "Save & Verify",
                            onClick = viewModel::saveApiKey,
                            enabled = !ui.verifying
                        )
                        if (ui.hasApiKey) {
                            Spacer(Modifier.height(Space.sm))
                            TextButton(onClick = viewModel::removeApiKey) {
                                Text("Remove key", color = ErrorRed)
                            }
                        }
                    }
                }

                // Voice prefs
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(Space.lg)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Voice output",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = prefs.voiceOutputEnabled,
                                onCheckedChange = viewModel::setVoiceOutput
                            )
                        }
                        Spacer(Modifier.height(Space.sm))
                        Text(
                            text = "Speech rate: ${(prefs.speechRate * 100).roundToInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Slider(
                            value = prefs.speechRate,
                            onValueChange = { viewModel.setSpeechRate(it) },
                            valueRange = 0.5f..2.0f,
                            steps = 14
                        )
                        Spacer(Modifier.height(Space.sm))
                        Text(
                            text = "Speech language: ${prefs.speechLanguage}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Space.sm),
                            modifier = Modifier.padding(top = Space.sm)
                        ) {
                            LangChip("English", prefs.speechLanguage == "en-US") {
                                viewModel.setSpeechLanguage("en-US")
                            }
                            LangChip("Urdu", prefs.speechLanguage == "ur-PK") {
                                viewModel.setSpeechLanguage("ur-PK")
                            }
                        }
                    }
                }

                // Preferred response language
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(Space.lg)) {
                        Text(
                            text = "Response language",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Spacer(Modifier.height(Space.sm))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Space.sm)
                        ) {
                            LangChip("Auto", prefs.preferredLanguage == "auto") {
                                viewModel.setPreferredLanguage("auto")
                            }
                            LangChip("English", prefs.preferredLanguage == "en") {
                                viewModel.setPreferredLanguage("en")
                            }
                            LangChip("Roman Urdu", prefs.preferredLanguage == "roman_ur") {
                                viewModel.setPreferredLanguage("roman_ur")
                            }
                            LangChip("اردو", prefs.preferredLanguage == "ur") {
                                viewModel.setPreferredLanguage("ur")
                            }
                        }
                    }
                }

                // Privacy
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(Space.lg)) {
                        Text(
                            text = "Privacy & data",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Spacer(Modifier.height(Space.sm))
                        Text(
                            text = "Conversation history is stored locally. Chat turns are sent to Google Gemini for inference only.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Spacer(Modifier.height(Space.md))
                        TextButton(onClick = { showEraseDialog = true }) {
                            Text("Erase local data", color = ErrorRed)
                        }
                    }
                }

                // About
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(Space.lg)) {
                        Text(
                            text = "About",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Spacer(Modifier.height(Space.sm))
                        Text(
                            text = "JARVIS AI · v1.0.0",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Text(
                            text = "Model: gemini-1.5-flash",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
                Spacer(Modifier.height(Space.xxl))
            }
        }
    }

    if (showEraseDialog) {
        AlertDialog(
            onDismissRequest = { showEraseDialog = false },
            title = { Text("Erase all local data?") },
            text = { Text("Conversation history, reminders, and the API key will be deleted.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eraseAllLocalData()
                    showEraseDialog = false
                }) { Text("Erase", color = ErrorRed) }
            },
            dismissButton = {
                TextButton(onClick = { showEraseDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun LangChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) NeonCyan.copy(alpha = 0.18f) else Color(0x14FFFFFF)
    val borderColor = if (selected) NeonCyan else Color(0x1AFFFFFF)
    val textColor = if (selected) NeonCyan else TextSecondary
    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
