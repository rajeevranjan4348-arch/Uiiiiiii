package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SyncStateInfo
import com.example.model.VoiceModeStatus
import com.example.model.VoiceSettings
import com.example.ui.components.AnimatedAIOrb
import com.example.ui.components.VoiceSettingsPopup
import com.example.ui.theme.KimiBackground
import com.example.ui.theme.KimiBlueGlow
import com.example.ui.theme.KimiBluePrimary
import com.example.ui.theme.KimiBorderHighlight
import com.example.ui.theme.KimiBorderSubtle
import com.example.ui.theme.KimiGreenActive
import com.example.ui.theme.KimiIconCircle
import com.example.ui.theme.KimiIconCircleBorder
import com.example.ui.theme.KimiRedEndCall
import com.example.ui.theme.KimiSurfaceCard
import com.example.ui.theme.KimiSurfaceCardSubtle
import com.example.ui.theme.KimiTextMuted
import com.example.ui.theme.KimiTextPrimary
import com.example.ui.theme.KimiTextSecondary
import com.example.ui.theme.KimiTextWhite

@Composable
fun VoiceModeScreen(
  modifier: Modifier = Modifier,
  voiceStatus: VoiceModeStatus,
  responseText: String,
  isVoiceSettingsOpen: Boolean,
  isKeyboardOpen: Boolean,
  keyboardInput: String,
  voiceSettings: VoiceSettings,
  syncState: SyncStateInfo = SyncStateInfo(),
  onBackToChat: () -> Unit,
  onToggleSettings: () -> Unit,
  onUpdateSettings: ((VoiceSettings) -> VoiceSettings) -> Unit,
  onForceSync: () -> Unit = {},
  onTogglePause: () -> Unit,
  onEndCall: () -> Unit,
  onInterrupt: () -> Unit,
  onToggleKeyboard: (Boolean?) -> Unit,
  onKeyboardInputChange: (String) -> Unit,
  onSendKeyboardMessage: (String) -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition(label = "VoiceAtmosphere")
  val bgPulseScale by infiniteTransition.animateFloat(
    initialValue = 0.92f,
    targetValue = 1.08f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "BgPulse"
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFF090E17),
            Color(0xFF0D1424),
            Color(0xFF0B0E14)
          )
        )
      )
      .statusBarsPadding()
      .navigationBarsPadding()
      .imePadding()
  ) {
    // Large ambient atmospheric glowing background behind center orb
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp),
      contentAlignment = Alignment.Center
    ) {
      Box(
        modifier = Modifier
          .size(340.dp * bgPulseScale)
          .clip(CircleShape)
          .background(
            Brush.radialGradient(
              colors = listOf(
                Color(0xFF1B4E9B).copy(alpha = 0.55f),
                Color(0xFF133675).copy(alpha = 0.35f),
                Color(0xFF0E2248).copy(alpha = 0.15f),
                Color.Transparent
              )
            )
          )
      )
    }

    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp, vertical = 12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // TOP BAR (Screenshot 3 & 8)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Left: Chat icon / Return to chat
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(KimiIconCircle)
            .border(1.dp, KimiIconCircleBorder, RoundedCornerShape(12.dp))
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = ripple(bounded = true),
              onClick = onBackToChat
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Outlined.Chat,
            contentDescription = "Chat",
            tint = KimiTextWhite,
            modifier = Modifier.size(22.dp)
          )
        }

        // Center Title & Subtitle
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "Kimi",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              color = KimiTextWhite
            )
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Generated by Kimi AI",
            style = MaterialTheme.typography.bodySmall.copy(
              fontSize = 12.sp,
              color = KimiTextMuted
            )
          )
        }

        // Right: Green Mic status pill + Settings button
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Green mic pill badge
          Box(
            modifier = Modifier
              .height(28.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(KimiGreenActive)
              .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Mic active",
                tint = Color.White,
                modifier = Modifier.size(15.dp)
              )
              Text(
                text = "60",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp
                )
              )
            }
          }

          // Settings sliders button [⊶⊷]
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(KimiIconCircle)
              .border(1.dp, KimiIconCircleBorder, CircleShape)
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onToggleSettings
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.Tune,
              contentDescription = "Voice settings",
              tint = KimiTextWhite,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.weight(1f))

      // CENTER ORB & CONVERSATION TEXT
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
          .fillMaxWidth()
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onInterrupt
          )
      ) {
        // Large Glowing AI Orb
        AnimatedAIOrb(
          size = 140.dp,
          isVoiceMode = true,
          voiceStatus = voiceStatus
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Dynamic Conversation Text
        Text(
          text = responseText,
          style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 32.sp,
            color = KimiTextWhite
          ),
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(horizontal = 24.dp)
        )

        // Dynamic Voice Waveform reacting in real-time to microphone audio
        Spacer(modifier = Modifier.height(20.dp))
        DynamicVoiceWaveform(voiceStatus = voiceStatus)
      }

      Spacer(modifier = Modifier.weight(1.2f))

      // BOTTOM CONTROLS SECTION
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        // "Tap to interrupt" indication
        if (voiceStatus == VoiceModeStatus.SPEAKING) {
          Text(
            text = "Tap to interrupt",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = KimiTextSecondary.copy(alpha = 0.8f),
              fontSize = 14.5.sp
            ),
            modifier = Modifier
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false),
                onClick = onInterrupt
              )
              .padding(vertical = 8.dp)
          )
          Spacer(modifier = Modifier.height(14.dp))
        } else {
          Spacer(modifier = Modifier.height(32.dp))
        }

        // Circular Action Buttons: Pause & End Call
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Pause / Resume Button
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = null,
              onClick = onTogglePause
            )
          ) {
            Box(
              modifier = Modifier
                .size(72.dp)
                .shadow(12.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.6f))
                .clip(CircleShape)
                .background(Color(0xFF202636))
                .border(1.dp, KimiBorderHighlight, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (voiceStatus == VoiceModeStatus.PAUSED) Icons.Default.PlayArrow else Icons.Default.Pause,
                contentDescription = "Pause",
                tint = KimiTextWhite,
                modifier = Modifier.size(32.dp)
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = if (voiceStatus == VoiceModeStatus.PAUSED) "Resume" else "Pause",
              style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 14.sp,
                color = KimiTextSecondary
              )
            )
          }

          // End Call Button (Red Phone)
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = null,
              onClick = onEndCall
            )
          ) {
            Box(
              modifier = Modifier
                .size(72.dp)
                .shadow(12.dp, CircleShape, spotColor = KimiRedEndCall.copy(alpha = 0.4f))
                .clip(CircleShape)
                .background(Color(0xFF282834))
                .border(1.dp, KimiBorderHighlight, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CallEnd,
                contentDescription = "End call",
                tint = KimiRedEndCall,
                modifier = Modifier.size(34.dp)
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "End",
              style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 14.sp,
                color = KimiTextSecondary
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // "Tap to show keyboard"
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = ripple(bounded = true),
              onClick = { onToggleKeyboard(true) }
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Outlined.Keyboard,
            contentDescription = "Keyboard",
            tint = KimiTextSecondary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Tap to show keyboard",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = KimiTextSecondary,
              fontSize = 14.5.sp
            )
          )
        }

        Spacer(modifier = Modifier.height(8.dp))
      }
    }

    // Voice Keyboard Input Overlay
    if (isKeyboardOpen) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black.copy(alpha = 0.6f))
          .clickable { onToggleKeyboard(false) },
        contentAlignment = Alignment.BottomCenter
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {}
            .background(KimiSurfaceCard, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            BasicTextField(
              value = keyboardInput,
              onValueChange = onKeyboardInputChange,
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .background(KimiSurfaceCardSubtle, RoundedCornerShape(24.dp))
                .border(1.dp, KimiBorderSubtle, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
              textStyle = TextStyle(color = KimiTextWhite, fontSize = 15.sp),
              cursorBrush = SolidColor(KimiBluePrimary),
              keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
              keyboardActions = KeyboardActions(
                onSend = {
                  onSendKeyboardMessage(keyboardInput)
                }
              ),
              decorationBox = { innerTextField ->
                if (keyboardInput.isEmpty()) {
                  Text(
                    text = "Type a message to Kimi...",
                    style = MaterialTheme.typography.bodyMedium.copy(color = KimiTextMuted)
                  )
                }
                innerTextField()
              }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(KimiBluePrimary)
                .clickable {
                  onSendKeyboardMessage(keyboardInput)
                },
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(19.dp)
              )
            }
          }
        }
      }
    }

    // Voice Settings Popup Floating Overlay
    if (isVoiceSettingsOpen) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .clickable { onToggleSettings() }
      ) {
        VoiceSettingsPopup(
          modifier = Modifier
            .padding(top = 56.dp, end = 16.dp)
            .align(Alignment.TopEnd),
          isOpen = isVoiceSettingsOpen,
          settings = voiceSettings,
          syncState = syncState,
          onUpdateSettings = onUpdateSettings,
          onForceSync = onForceSync,
          onDismiss = onToggleSettings
        )
      }
    }
  }
}

@Composable
fun DynamicVoiceWaveform(
  modifier: Modifier = Modifier,
  voiceStatus: VoiceModeStatus = VoiceModeStatus.LISTENING,
  barCount: Int = 23
) {
  val infiniteTransition = rememberInfiniteTransition(label = "VoiceWaveformTransition")

  val phase1 by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = (2 * Math.PI).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 1100, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "WavePhase1"
  )

  val phase2 by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = (2 * Math.PI).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 800, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "WavePhase2"
  )

  val phase3 by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = (2 * Math.PI).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 1500, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "WavePhase3"
  )

  val isActive = voiceStatus == VoiceModeStatus.LISTENING || voiceStatus == VoiceModeStatus.SPEAKING
  val isListening = voiceStatus == VoiceModeStatus.LISTENING

  // Elastic spring physics for status transitions
  val smoothActivityFactor by animateFloatAsState(
    targetValue = if (isActive) 1.0f else 0.15f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    ),
    label = "SmoothActivityFactor"
  )

  val smoothListeningFactor by animateFloatAsState(
    targetValue = if (isListening) 1.0f else 0.65f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioLowBouncy,
      stiffness = Spring.StiffnessLow
    ),
    label = "SmoothListeningFactor"
  )

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(24.dp))
      .background(Color(0xFF131A2B).copy(alpha = 0.65f))
      .border(1.dp, KimiBorderSubtle.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
      .padding(horizontal = 20.dp, vertical = 12.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(4.5.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      for (i in 0 until barCount) {
        val normalizedIndex = (i.toFloat() / (barCount - 1)) * 2f - 1f
        val envelope = kotlin.math.exp((-2.8f * normalizedIndex * normalizedIndex).toDouble()).toFloat()

        val waveA = kotlin.math.sin((phase1 + i * 0.45f).toDouble()).toFloat()
        val waveB = kotlin.math.cos((phase2 - i * 0.60f).toDouble()).toFloat()
        val waveC = kotlin.math.sin((phase3 + i * 0.30f).toDouble()).toFloat()
        val combinedWave = (waveA * 0.40f + waveB * 0.35f + waveC * 0.25f + 1.0f) / 2.0f

        val baseMaxHeight = 44.dp * smoothListeningFactor
        val minBarHeight = 6.dp

        val dynamicAmplification = (0.18f + 0.82f * combinedWave * envelope) * smoothActivityFactor
        val barHeightDp = minBarHeight + (baseMaxHeight - minBarHeight) * dynamicAmplification

        Box(
          modifier = Modifier
            .width(3.5.dp)
            .height(barHeightDp)
            .clip(CircleShape)
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color(0xFF93C5FD),
                  KimiBluePrimary,
                  Color(0xFF1E3A8A)
                )
              )
            )
        )
      }
    }
  }
}

@Composable
private fun AnimatedDots() {
  val infiniteTransition = rememberInfiniteTransition(label = "LoadingDots")
  val dotProgress by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 7f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 1400, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "DotProgress"
  )

  Row(
    horizontalArrangement = Arrangement.spacedBy(6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    for (i in 0 until 7) {
      val distance = kotlin.math.abs(dotProgress - i)
      val alpha = if (distance < 1.5f) 0.95f else 0.35f
      val scale = if (distance < 1.5f) 1.25f else 0.85f

      Box(
        modifier = Modifier
          .size((6 * scale).dp)
          .clip(CircleShape)
          .background(Color.White.copy(alpha = alpha))
      )
    }
  }
}
