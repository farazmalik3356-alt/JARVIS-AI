package com.jarvis.ai.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jarvis.ai.ui.theme.BorderSubtle
import com.jarvis.ai.ui.theme.SurfaceGlass

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadiusDp: Int = 20,
    glowCyan: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadiusDp.dp)
    val borderColor = if (glowCyan) Color(0x6600F0FF) else BorderSubtle
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(
                        SurfaceGlass,
                        SurfaceGlass.copy(alpha = SurfaceGlass.alpha * 0.4f)
                    )
                )
            )
            .border(BorderStroke(1.dp, borderColor), shape)
            .padding(1.dp)
    ) {
        Box(modifier = Modifier.padding(0.dp)) { content() }
    }
}