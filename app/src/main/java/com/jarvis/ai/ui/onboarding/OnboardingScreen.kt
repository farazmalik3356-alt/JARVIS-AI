package com.jarvis.ai.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jarvis.ai.ui.components.AiOrb
import com.jarvis.ai.ui.components.GradientBackground
import com.jarvis.ai.ui.components.PrimaryButton
import com.jarvis.ai.ui.components.SecondaryButton
import com.jarvis.ai.ui.theme.Space
import com.jarvis.ai.ui.theme.TextPrimary
import com.jarvis.ai.ui.theme.TextSecondary

@Composable
fun OnboardingScreen(
    onContinue: () -> Unit,
    onSkip: () -> Unit
) {
    GradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Space.xl, vertical = Space.xxl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AiOrb(size = 220.dp)
            Spacer(Modifier.height(Space.xxl))
            Text(
                text = "JARVIS AI",
                style = MaterialTheme.typography.displayMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(Space.sm))
            Text(
                text = "Your Intelligent Assistant",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(Space.xxl))
            Text(
                text = "Speak, type, or issue commands. JARVIS understands English, Urdu, and Roman Urdu, and can perform supported device actions with your permission.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(Space.xxxl))
            PrimaryButton(text = "Continue", onClick = onContinue)
            Spacer(Modifier.height(Space.md))
            SecondaryButton(text = "Configure later", onClick = onSkip)
            Spacer(Modifier.height(Space.xl))
        }
    }
}