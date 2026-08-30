package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
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
import com.example.model.ThinkingEffort
import com.example.ui.theme.KimiBluePrimary
import com.example.ui.theme.KimiBorderHighlight
import com.example.ui.theme.KimiBorderSubtle
import com.example.ui.theme.KimiSurfaceCard
import com.example.ui.theme.KimiTextMuted
import com.example.ui.theme.KimiTextPrimary
import com.example.ui.theme.KimiTextSecondary
import com.example.ui.theme.KimiTextWhite

@Composable
fun ModelSelectorPopup(
  modifier: Modifier = Modifier,
  isOpen: Boolean,
  selectedModel: ModelOption,
  thinkingEffort: ThinkingEffort,
  onSelectModel: (ModelOption) -> Unit,
  onCycleThinkingEffort: () -> Unit,
  onDismiss: () -> Unit
) {
  AnimatedVisibility(
    visible = isOpen,
    enter = fadeIn(tween(180)) + scaleIn(tween(180), initialScale = 0.92f),
    exit = fadeOut(tween(120)) + scaleOut(tween(120), targetScale = 0.92f)
  ) {
    Box(
      modifier = modifier
        .width(310.dp)
        .shadow(16.dp, RoundedCornerShape(24.dp), spotColor = Color.Black.copy(alpha = 0.7f))
        .clip(RoundedCornerShape(24.dp))
        .background(KimiSurfaceCard)
        .border(1.dp, KimiBorderHighlight, RoundedCornerShape(24.dp))
        .padding(vertical = 12.dp)
    ) {
      Column {
        // Model Option 1: K3
        ModelRow(
          title = ModelOption.K3.title,
          subtitle = ModelOption.K3.subtitle,
          isSelected = selectedModel == ModelOption.K3,
          onClick = { onSelectModel(ModelOption.K3) }
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Model Option 2: K3 Swarm
        ModelRow(
          title = ModelOption.K3_SWARM.title,
          subtitle = ModelOption.K3_SWARM.subtitle,
          isSelected = selectedModel == ModelOption.K3_SWARM,
          onClick = { onSelectModel(ModelOption.K3_SWARM) }
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Model Option 3: Instant
        ModelRow(
          title = ModelOption.INSTANT.title,
          subtitle = ModelOption.INSTANT.subtitle,
          isSelected = selectedModel == ModelOption.INSTANT,
          onClick = { onSelectModel(ModelOption.INSTANT) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Divider
        HorizontalDivider(
          modifier = Modifier.padding(horizontal = 16.dp),
          thickness = 0.8.dp,
          color = KimiBorderSubtle
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Thinking Effort Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = ripple(bounded = true),
              onClick = onCycleThinkingEffort
            )
            .padding(horizontal = 20.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text(
              text = "Thinking effort",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = KimiTextWhite
              )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = thinkingEffort.displayName,
              style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                color = KimiTextSecondary
              )
            )
          }

          Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Change thinking effort",
            tint = KimiTextMuted,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun ModelRow(
  title: String,
  subtitle: String,
  isSelected: Boolean,
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
      .padding(horizontal = 20.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
          color = KimiTextWhite
        )
      )
      Spacer(modifier = Modifier.height(3.dp))
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyMedium.copy(
          fontSize = 13.5.sp,
          lineHeight = 18.sp,
          color = KimiTextSecondary
        )
      )
    }

    if (isSelected) {
      Icon(
        imageVector = Icons.Default.Check,
        contentDescription = "Selected",
        tint = KimiBluePrimary,
        modifier = Modifier
          .size(24.dp)
          .padding(start = 8.dp)
      )
    }
  }
}
