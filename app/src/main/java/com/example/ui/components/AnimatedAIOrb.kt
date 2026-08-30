package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.model.VoiceModeStatus
import kotlin.math.sin

@Composable
fun AnimatedAIOrb(
  modifier: Modifier = Modifier,
  size: Dp = 64.dp,
  isVoiceMode: Boolean = false,
  voiceStatus: VoiceModeStatus = VoiceModeStatus.SPEAKING
) {
  val infiniteTransition = rememberInfiniteTransition(label = "OrbTransition")

  // Ultra-smooth dual-axis floating animation (Lissajous curve)
  val floatPhaseY by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = (2 * Math.PI).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 3200, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "FloatPhaseY"
  )

  val floatPhaseX by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = (2 * Math.PI).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 4800, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "FloatPhaseX"
  )

  // Gentle breathing scale loop
  val breathRaw by infiniteTransition.animateFloat(
    initialValue = 0.97f,
    targetValue = 1.03f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "BreathRaw"
  )

  // Smooth state-driven multiplier using spring physics
  val statusScaleMultiplier by animateFloatAsState(
    targetValue = when (voiceStatus) {
      VoiceModeStatus.SPEAKING -> 1.08f
      VoiceModeStatus.LISTENING -> 1.02f
      VoiceModeStatus.PAUSED -> 0.94f
      VoiceModeStatus.THINKING -> 1.05f
    },
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    ),
    label = "StatusScaleSpring"
  )

  // Smooth glow transition for voice status
  val glowAlphaTarget = when (voiceStatus) {
    VoiceModeStatus.SPEAKING -> 0.90f
    VoiceModeStatus.LISTENING -> 0.70f
    VoiceModeStatus.PAUSED -> 0.35f
    VoiceModeStatus.THINKING -> 0.85f
  }
  val smoothGlowBase by animateFloatAsState(
    targetValue = glowAlphaTarget,
    animationSpec = spring(stiffness = Spring.StiffnessLow),
    label = "SmoothGlowBase"
  )

  val glowPulse by infiniteTransition.animateFloat(
    initialValue = 0.85f,
    targetValue = 1.15f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "GlowPulse"
  )

  // Natural periodic eye blink animation
  val blinkCycle by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 3600, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "EyeBlinkCycle"
  )

  // Eye height ratio with sudden smooth close and open for a natural wink/blink
  val eyeHeightRatio = if (blinkCycle in 0.88f..0.94f) {
    val progress = (blinkCycle - 0.88f) / 0.06f
    if (progress < 0.5f) {
      1.0f - (progress * 2.0f) * 0.85f // Close eye smoothly
    } else {
      0.15f + ((progress - 0.5f) * 2.0f) * 0.85f // Reopen eye smoothly
    }
  } else {
    1.0f
  }

  val floatOffsetY = sin(floatPhaseY.toDouble()).toFloat() * 3.5f
  val floatOffsetX = sin(floatPhaseX.toDouble()).toFloat() * 2.0f

  Box(
    modifier = modifier.size(size),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val centerOffset = Offset(
        x = this.size.width / 2f + floatOffsetX * (this.size.width / 64f),
        y = this.size.height / 2f + floatOffsetY * (this.size.height / 64f)
      )

      val effectiveRadius = (this.size.minDimension / 2f) * breathRaw * statusScaleMultiplier * (if (isVoiceMode) 0.72f else 0.78f)
      val effectiveGlow = smoothGlowBase * glowPulse

      // Outer radial aura / glow
      val outerGlowRadius = if (isVoiceMode) effectiveRadius * 2.85f else effectiveRadius * 1.6f
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(
            Color(0xFF2F80ED).copy(alpha = (if (isVoiceMode) 0.45f * effectiveGlow else 0.28f).coerceIn(0f, 1f)),
            Color(0xFF1E5BB5).copy(alpha = (if (isVoiceMode) 0.22f * effectiveGlow else 0.12f).coerceIn(0f, 1f)),
            Color.Transparent
          ),
          center = centerOffset,
          radius = outerGlowRadius
        ),
        radius = outerGlowRadius,
        center = centerOffset
      )

      // Mid aura
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(
            Color(0xFF4FA0FF).copy(alpha = (0.5f * effectiveGlow).coerceIn(0f, 1f)),
            Color(0xFF2F80ED).copy(alpha = 0.2f),
            Color.Transparent
          ),
          center = centerOffset,
          radius = effectiveRadius * 1.32f
        ),
        radius = effectiveRadius * 1.32f,
        center = centerOffset
      )

      // Main 3D Sphere body
      val sphereGradient = Brush.radialGradient(
        colors = listOf(
          Color(0xFF88CEFF), // Top-left specular highlight
          Color(0xFF388BF2), // Vibrant core
          Color(0xFF1B64D8), // Mid shadow
          Color(0xFF0F439C)  // Bottom right dark ambient
        ),
        center = Offset(centerOffset.x - effectiveRadius * 0.28f, centerOffset.y - effectiveRadius * 0.32f),
        radius = effectiveRadius * 1.4f
      )

      drawCircle(
        brush = sphereGradient,
        radius = effectiveRadius,
        center = centerOffset
      )

      // Subtle specular rim stroke
      drawCircle(
        brush = Brush.linearGradient(
          colors = listOf(
            Color.White.copy(alpha = 0.60f),
            Color(0xFF4A90E2).copy(alpha = 0.25f),
            Color(0xFF0A2B68).copy(alpha = 0.65f)
          ),
          start = Offset(centerOffset.x - effectiveRadius, centerOffset.y - effectiveRadius),
          end = Offset(centerOffset.x + effectiveRadius, centerOffset.y + effectiveRadius)
        ),
        radius = effectiveRadius,
        center = centerOffset,
        style = Stroke(width = effectiveRadius * 0.045f)
      )

      // Glossy cute eyes
      val eyeWidth = effectiveRadius * 0.16f
      val eyeHeight = effectiveRadius * 0.32f * eyeHeightRatio
      val eyeY = centerOffset.y - effectiveRadius * 0.14f
      val leftEyeX = centerOffset.x - effectiveRadius * 0.26f
      val rightEyeX = centerOffset.x + effectiveRadius * 0.06f

      // Left Eye
      drawRoundRect(
        color = Color.White.copy(alpha = 0.95f),
        topLeft = Offset(leftEyeX - eyeWidth / 2f, eyeY - eyeHeight / 2f),
        size = Size(eyeWidth, eyeHeight),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(eyeWidth / 2f, eyeWidth / 2f)
      )

      // Right Eye
      drawRoundRect(
        color = Color.White.copy(alpha = 0.95f),
        topLeft = Offset(rightEyeX - eyeWidth / 2f, eyeY - eyeHeight / 2f),
        size = Size(eyeWidth, eyeHeight),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(eyeWidth / 2f, eyeWidth / 2f)
      )
    }
  }
}

