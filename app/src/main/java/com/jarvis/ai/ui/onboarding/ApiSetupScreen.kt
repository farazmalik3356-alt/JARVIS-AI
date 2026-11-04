package com.jarvis.ai.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jarvis.ai.ui.components.GradientBackground
import com.jarvis.ai.ui.theme.ErrorRed
import com.jarvis.ai.ui.theme.NeonCyan
import com.jarvis.ai.ui.theme.TextPrimary
import com.jarvis.ai.ui.theme.TextSecondary

@Composable
fun ApiSetupScreen(
    onDone: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val ui by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    // Jab verification kaamyab ho jaye to agla screen kholo
    if (ui.success) {
        onDone()
    }

    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Connect OpenRouter",
                style = MaterialTheme.typography.headlineMedium,
                color = NeonCyan
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "JARVIS uses OpenRouter to power its AI. Get a free API key from OpenRouter.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = { uriHandler.openUri("https://openrouter.ai/keys") }) {
                Text("Get OpenRouter API Key →", color = NeonCyan)
            }
            Spacer(Modifier.height(24.dp))
            TextField(
                value = ui.keyInput,
                onValueChange = viewModel::onKeyChange,
                placeholder = { Text("sk-or-v1-...") },
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
            if (ui.error != null) {
                Spacer(Modifier.height(8.dp))
                Text(ui.error!!, color = ErrorRed)
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = viewModel::saveAndVerify,
                enabled = !ui.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (ui.isSaving) "Verifying..." else "Save & Continue")
            }
        }
    }
}
