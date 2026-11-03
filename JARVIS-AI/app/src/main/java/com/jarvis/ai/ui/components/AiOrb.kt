package com.jarvis.ai.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jarvis.ai.ui.theme.ElectricViolet
import com.jarvis.ai.ui.theme.NeonCyan
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AiOrb(
    modifier: Modifier = Modifier,
    size: Dp = 180.dp,
    speaking: Boolean = false,
    listening: Boolean = false
) {
    val pulse = rememberInfiniteTransition(label = "orb-pulse")
    val base by pulse.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb-pulse-scale"
    )
    val ringRotation by pulse.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orb-ring-rotation"
    )
    val glowAlpha = when {
        speaking -> 0.55f
        listening -> 0.40f
        else -> 0.30f
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val baseRadius = this.size.minDimension * 0.30f * base

            // Outer glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NeonCyan.copy(alpha = glowAlpha),
                        ElectricViolet.copy(alpha = glowAlpha * 0.6f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = this.size.minDimension * 0.5f
                ),
                radius = this.size.minDimension * 0.5f,
                center = center
            )

            // Inner core
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White, NeonCyan, ElectricViolet),
                    center = center,
                    radius = baseRadius
                ),
                radius = baseRadius,
                center = center
            )

            // Rotating dashed rings
            val ringRadius = baseRadius * 1.55f
            val dashCount = 24
            val arcSize = (360f / dashCount) * 0.45f
            for (i in 0 until dashCount) {
                val angleDeg = (i * (360f / dashCount)) + ringRotation
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val x = center.x + (ringRadius * cos(angleRad)).toFloat()
                val y = center.y + (ringRadius * sin(angleRad)).toFloat()
                val arcRadius = baseRadius * 0.10f
                drawArc(
                    color = if (i % 2 == 0) NeonCyan.copy(alpha = 0.85f)
                    else ElectricViolet.copy(alpha = 0.85f),
                    startAngle = angleDeg,
                    sweepAngle = arcSize,
                    useCenter = false,
                    topLeft = Offset(x - arcRadius, y - arcRadius),
                    size = Size(arcRadius * 2, arcRadius * 2),
                    style = Stroke(width = 2f)
                )
            }

            // Secondary thin ring
            drawCircle(
                color = Color(0x55FFFFFF),
                radius = baseRadius * 1.9f,
                center = center,
                style = Stroke(width = 1.2f)
            )
        }
    }
}