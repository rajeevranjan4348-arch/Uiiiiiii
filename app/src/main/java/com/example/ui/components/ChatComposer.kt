package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KimiBluePrimary
import com.example.ui.theme.KimiBorderSubtle
import com.example.ui.theme.KimiIconCircle
import com.example.ui.theme.KimiIconCircleBorder
import com.example.ui.theme.KimiSurfaceCard
import com.example.ui.theme.KimiSurfaceCardSubtle
import com.example.ui.theme.KimiTextMuted
import com.example.ui.theme.KimiTextPrimary
import com.example.ui.theme.KimiTextSecondary
import com.example.ui.theme.KimiTextWhite

@Composable
fun ChatComposer(
  modifier: Modifier = Modifier,
  text: String,
  onTextChange: (String) -> Unit,
  onSend: () -> Unit,
  onPlusClick: () -> Unit,
  onVoiceClick: () -> Unit
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(58.dp)
        .shadow(8.dp, RoundedCornerShape(29.dp), spotColor = Color.Black.copy(alpha = 0.5f))
        .clip(RoundedCornerShape(29.dp))
        .background(KimiSurfaceCard)
        .border(1.dp, KimiBorderSubtle, RoundedCornerShape(29.dp))
        .padding(horizontal = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Left: Plus Button
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(KimiSurfaceCardSubtle)
          .border(1.dp, KimiIconCircleBorder, CircleShape)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = true),
            onClick = onPlusClick
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Attach files and tools",
          tint = KimiTextSecondary,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      // Center: Input TextField with Placeholder
      Box(
        modifier = Modifier
          .weight(1f)
          .padding(vertical = 4.dp),
        contentAlignment = Alignment.CenterStart
      ) {
        if (text.isEmpty()) {
          Text(
            text = "Ask anything. Images work too.",
            style = MaterialTheme.typography.bodyLarge.copy(
              color = KimiTextMuted,
              fontSize = 15.5.sp
            ),
            maxLines = 1
          )
        }

        BasicTextField(
          value = text,
          onValueChange = onTextChange,
          modifier = Modifier.fillMaxWidth(),
          textStyle = TextStyle(
            color = KimiTextWhite,
            fontSize = 15.5.sp,
            fontWeight = FontWeight.Normal
          ),
          cursorBrush = SolidColor(KimiBluePrimary),
          maxLines = 2,
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
          keyboardActions = KeyboardActions(onSend = { onSend() })
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      // Right: Voice Button or Send Button
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(if (text.isNotBlank()) KimiBluePrimary else KimiSurfaceCardSubtle)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = true),
            onClick = {
              if (text.isNotBlank()) onSend() else onVoiceClick()
            }
          ),
        contentAlignment = Alignment.Center
      ) {
        if (text.isNotBlank()) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Send message",
            tint = KimiTextWhite,
            modifier = Modifier.size(19.dp)
          )
        } else {
          // Waveform / Voice icon
          Icon(
            imageVector = Icons.Default.GraphicEq,
            contentDescription = "Voice mode",
            tint = KimiTextSecondary,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}
