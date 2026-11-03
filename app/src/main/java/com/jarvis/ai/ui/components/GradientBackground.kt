package com.jarvis.ai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.jarvis.ai.ui.theme.DeepNavy
import com.jarvis.ai.ui.theme.ElectricVioletDim
import com.jarvis.ai.ui.theme.NeonCyanDim
import com.jarvis.ai.ui.theme.VoidBlack

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        DeepNavy,
                        NeonCyanDim.copy(alpha = 0.08f),
                        ElectricVioletDim.copy(alpha = 0.10f),
                        VoidBlack
                    ),
                    center = Offset(0f, 0f),
                    radius = 2000f
                )
            )
    ) {
        content()
    }
}