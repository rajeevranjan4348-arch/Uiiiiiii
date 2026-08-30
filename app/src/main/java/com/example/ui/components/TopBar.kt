package com.example.ui.components

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
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ModelOption
import com.example.ui.theme.KimiBorderSubtle
import com.example.ui.theme.KimiIconCircle
import com.example.ui.theme.KimiIconCircleBorder
import com.example.ui.theme.KimiPillBackground
import com.example.ui.theme.KimiPillBorder
import com.example.ui.theme.KimiTextPrimary
import com.example.ui.theme.KimiTextSecondary
import com.example.ui.theme.KimiTextWhite

@Composable
fun TopBar(
  modifier: Modifier = Modifier,
  selectedModel: ModelOption,
  isMuted: Boolean,
  onMenuClick: () -> Unit,
  onModelSelectorClick: () -> Unit,
  onSpeakerClick: () -> Unit
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    // Left: Hamburger Menu Button
    Box(
      modifier = Modifier
        .size(44.dp)
        .shadow(4.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.5f))
        .clip(CircleShape)
        .background(KimiIconCircle)
        .border(1.dp, KimiIconCircleBorder, CircleShape)
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = ripple(bounded = true),
          onClick = onMenuClick
        ),
      contentAlignment = Alignment.Center
    ) {
      Column(
        verticalArrangement = Arrangement.spacedBy(3.5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .width(16.dp)
            .height(2.dp)
            .background(KimiTextPrimary, RoundedCornerShape(1.dp))
        )
        Box(
          modifier = Modifier
            .width(16.dp)
            .height(2.dp)
            .background(KimiTextPrimary, RoundedCornerShape(1.dp))
        )
        Box(
          modifier = Modifier
            .width(16.dp)
            .height(2.dp)
            .background(KimiTextPrimary, RoundedCornerShape(1.dp))
        )
      }
    }

    Spacer(modifier = Modifier.width(10.dp))

    // Center-Left: Model Selector Pill ("Kimi Instant")
    Box(
      modifier = Modifier
        .weight(1f, fill = false)
        .height(44.dp)
        .shadow(4.dp, RoundedCornerShape(22.dp), spotColor = Color.Black.copy(alpha = 0.4f))
        .clip(RoundedCornerShape(22.dp))
        .background(KimiPillBackground)
        .border(1.dp, KimiPillBorder, RoundedCornerShape(22.dp))
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = ripple(bounded = true),
          onClick = onModelSelectorClick
        )
        .padding(horizontal = 18.dp),
      contentAlignment = Alignment.Center
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = "Kimi",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = KimiTextWhite
          )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = when (selectedModel) {
            ModelOption.INSTANT -> "Instant"
            ModelOption.K3 -> "K3"
            ModelOption.K3_SWARM -> "Swarm"
          },
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            color = KimiTextSecondary
          )
        )
      }
    }

    Spacer(modifier = Modifier.weight(1f))

    // Right: Speaker / Audio Button
    Box(
      modifier = Modifier
        .size(44.dp)
        .shadow(4.dp, CircleShape, spotColor = Color.Black.copy(alpha = 0.5f))
        .clip(CircleShape)
        .background(KimiIconCircle)
        .border(1.dp, KimiIconCircleBorder, CircleShape)
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = ripple(bounded = true),
          onClick = onSpeakerClick
        ),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeMute else Icons.AutoMirrored.Filled.VolumeUp,
        contentDescription = "Mute / Unmute",
        tint = KimiTextPrimary,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}
