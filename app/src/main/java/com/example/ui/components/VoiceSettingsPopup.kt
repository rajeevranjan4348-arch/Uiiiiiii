package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Hearing
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.HorizontalDivider
import com.example.data.CloudSyncStatus
import com.example.data.SyncStateInfo
import com.example.model.VoiceSettings
import com.example.ui.theme.KimiBorderHighlight
import com.example.ui.theme.KimiGreenActive
import com.example.ui.theme.KimiSurfaceCard
import com.example.ui.theme.KimiSwitchThumb
import com.example.ui.theme.KimiSwitchTrackOff
import com.example.ui.theme.KimiSwitchTrackOn
import com.example.ui.theme.KimiTextPrimary
import com.example.ui.theme.KimiTextSecondary
import com.example.ui.theme.KimiTextWhite

@Composable
fun VoiceSettingsPopup(
  modifier: Modifier = Modifier,
  isOpen: Boolean,
  settings: VoiceSettings,
  syncState: SyncStateInfo = SyncStateInfo(),
  onUpdateSettings: ((VoiceSettings) -> VoiceSettings) -> Unit,
  onForceSync: () -> Unit = {},
  onDismiss: () -> Unit
) {
  AnimatedVisibility(
    visible = isOpen,
    enter = fadeIn(tween(220, easing = FastOutSlowInEasing)) + scaleIn(
      animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioLowBouncy),
      initialScale = 0.88f
    ),
    exit = fadeOut(tween(150, easing = FastOutSlowInEasing)) + scaleOut(
      animationSpec = tween(150, easing = FastOutSlowInEasing),
      targetScale = 0.90f
    )
  ) {
    Box(
      modifier = modifier
        .width(300.dp)
        .shadow(20.dp, RoundedCornerShape(24.dp), spotColor = Color.Black.copy(alpha = 0.8f))
        .clip(RoundedCornerShape(24.dp))
        .background(KimiSurfaceCard)
        .border(1.dp, KimiBorderHighlight, RoundedCornerShape(24.dp))
        .padding(vertical = 14.dp, horizontal = 18.dp)
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Firebase Cloud Sync Status Banner
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF161E2E))
            .border(1.dp, Color(0xFF2B384E), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            val statusIcon = when (syncState.status) {
              CloudSyncStatus.SYNCED -> Icons.Outlined.CloudDone
              CloudSyncStatus.SYNCING -> Icons.Outlined.Sync
              CloudSyncStatus.OFFLINE -> Icons.Outlined.CloudOff
            }
            val statusColor = when (syncState.status) {
              CloudSyncStatus.SYNCED -> KimiGreenActive
              CloudSyncStatus.SYNCING -> Color(0xFF60A5FA)
              CloudSyncStatus.OFFLINE -> KimiTextSecondary
            }

            Icon(
              imageVector = statusIcon,
              contentDescription = "Cloud Sync Status",
              tint = statusColor,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column {
              Text(
                text = syncState.message,
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.5.sp,
                  color = KimiTextWhite
                )
              )
              Text(
                text = "Synced: ${syncState.lastSyncedTimeText}",
                style = MaterialTheme.typography.bodySmall.copy(
                  fontSize = 10.5.sp,
                  color = KimiTextSecondary
                )
              )
            }
          }

          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(Color(0xFF243048))
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onForceSync
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Outlined.Refresh,
              contentDescription = "Sync Now",
              tint = KimiTextWhite,
              modifier = Modifier.size(15.dp)
            )
          }
        }

        HorizontalDivider(color = Color(0xFF283042), thickness = 1.dp)
        // Speech Rate Row
        SettingValueRow(
          icon = Icons.Outlined.Speed,
          title = "Speech rate",
          value = settings.speechRate,
          onClick = {
            val nextRate = when (settings.speechRate) {
              "0.8x" -> "1.0x"
              "1.0x" -> "1.2x"
              "1.2x" -> "1.5x"
              else -> "0.8x"
            }
            onUpdateSettings { it.copy(speechRate = nextRate) }
          }
        )

        // Voice Playback Row
        SettingValueRow(
          icon = Icons.Outlined.GraphicEq,
          title = "Voice playback",
          value = settings.voicePlayback,
          onClick = {
            val nextVoice = when (settings.voicePlayback) {
              "Tintin" -> "Chloe"
              "Chloe" -> "James"
              "James" -> "Nova"
              else -> "Tintin"
            }
            onUpdateSettings { it.copy(voicePlayback = nextVoice) }
          }
        )

        // Opening Toggle Row
        SettingToggleRow(
          icon = Icons.Outlined.ChatBubbleOutline,
          title = "Opening",
          isChecked = settings.opening,
          onCheckedChange = { checked ->
            onUpdateSettings { it.copy(opening = checked) }
          }
        )

        // Voice Interrupt Toggle Row
        SettingToggleRow(
          icon = Icons.Outlined.Hearing,
          title = "Voice Interrupt",
          isChecked = settings.voiceInterrupt,
          onCheckedChange = { checked ->
            onUpdateSettings { it.copy(voiceInterrupt = checked) }
          }
        )

        // Dark Mode Toggle Row
        SettingToggleRow(
          icon = Icons.Outlined.DarkMode,
          title = "Dark mode",
          isChecked = settings.darkMode,
          onCheckedChange = { checked ->
            onUpdateSettings { it.copy(darkMode = checked) }
          }
        )

        // Keyboard Input Toggle Row
        SettingToggleRow(
          icon = Icons.Outlined.Keyboard,
          title = "Keyboard input",
          isChecked = settings.keyboardInput,
          onCheckedChange = { checked ->
            onUpdateSettings { it.copy(keyboardInput = checked) }
          }
        )
      }
    }
  }
}

@Composable
private fun SettingValueRow(
  icon: ImageVector,
  title: String,
  value: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(bounded = true),
        onClick = onClick
      )
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = KimiTextWhite,
        modifier = Modifier.size(22.dp)
      )
      Spacer(modifier = Modifier.width(12.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.SemiBold,
          fontSize = 15.5.sp,
          color = KimiTextWhite
        )
      )
    }

    Text(
      text = value,
      style = MaterialTheme.typography.bodyMedium.copy(
        fontSize = 14.5.sp,
        fontWeight = FontWeight.Medium,
        color = KimiTextSecondary
      )
    )
  }
}

@Composable
private fun SettingToggleRow(
  icon: ImageVector,
  title: String,
  isChecked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 2.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = KimiTextWhite,
        modifier = Modifier.size(22.dp)
      )
      Spacer(modifier = Modifier.width(12.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.SemiBold,
          fontSize = 15.5.sp,
          color = KimiTextWhite
        )
      )
    }

    Switch(
      checked = isChecked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = KimiSwitchThumb,
        checkedTrackColor = KimiSwitchTrackOn,
        uncheckedThumbColor = KimiSwitchThumb,
        uncheckedTrackColor = KimiSwitchTrackOff,
        uncheckedBorderColor = Color.Transparent,
        checkedBorderColor = Color.Transparent
      )
    )
  }
}
