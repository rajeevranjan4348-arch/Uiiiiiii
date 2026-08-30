package com.example.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KimiBackground
import com.example.ui.theme.KimiBlueLight
import com.example.ui.theme.KimiBluePrimary
import com.example.ui.theme.KimiBorderHighlight
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
fun AddCommonPhraseScreen(
  modifier: Modifier = Modifier,
  initialContent: String = "Kimi. Please summarize the main point of the article in one concise sentence.",
  initialTrigger: String = "",
  onBack: () -> Unit,
  onSavePhrase: (String, String) -> Unit
) {
  var contentText by remember { mutableStateOf(initialContent) }
  var triggerText by remember { mutableStateOf(initialTrigger) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(KimiBackground)
      .statusBarsPadding()
      .navigationBarsPadding()
      .imePadding()
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
      // TOP BAR (Screenshot 6)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Back Button
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(KimiIconCircle)
            .border(1.dp, KimiIconCircleBorder, CircleShape)
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = ripple(bounded = true),
              onClick = onBack
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "Back",
            tint = KimiTextWhite,
            modifier = Modifier.size(24.dp)
          )
        }

        // Title
        Text(
          text = "Add common phrase",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 19.sp,
            color = KimiTextWhite
          )
        )

        // Save Button (Checkmark)
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(if (contentText.isNotBlank()) KimiSurfaceCardSubtle else KimiIconCircle)
            .border(1.dp, KimiIconCircleBorder, CircleShape)
            .clickable(
              interactionSource = remember { MutableInteractionSource() },
              indication = ripple(bounded = true),
              onClick = {
                if (contentText.isNotBlank()) {
                  onSavePhrase(contentText, triggerText)
                }
              }
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Save phrase",
            tint = if (contentText.isNotBlank()) KimiTextWhite else KimiTextMuted,
            modifier = Modifier.size(22.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(22.dp))

      // MAIN CONTENT CARD
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(KimiSurfaceCard)
          .border(1.dp, KimiBorderSubtle, RoundedCornerShape(20.dp))
          .padding(20.dp)
      ) {
        Column {
          Text(
            text = "Set the content frequently sent to Kimi as common phrases.",
            style = MaterialTheme.typography.bodyLarge.copy(
              fontSize = 16.sp,
              lineHeight = 22.sp,
              color = KimiTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "Example for reference.",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontSize = 15.sp,
              fontWeight = FontWeight.Medium,
              color = KimiBluePrimary
            ),
            modifier = Modifier.clickable {
              contentText = "Kimi. Please summarize the main point of the article in one concise sentence."
              triggerText = "summarize"
            }
          )

          Spacer(modifier = Modifier.height(20.dp))

          BasicTextField(
            value = contentText,
            onValueChange = { contentText = it },
            modifier = Modifier
              .fillMaxWidth()
              .height(140.dp),
            textStyle = TextStyle(
              color = KimiTextSecondary,
              fontSize = 15.5.sp,
              lineHeight = 22.sp
            ),
            cursorBrush = SolidColor(KimiBluePrimary),
            decorationBox = { innerTextField ->
              if (contentText.isEmpty()) {
                Text(
                  text = "Enter frequently used content...",
                  style = MaterialTheme.typography.bodyLarge.copy(color = KimiTextMuted)
                )
              }
              innerTextField()
            }
          )
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // TRIGGER WORD SECTION
      Text(
        text = "Trigger word (optional)",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Medium,
          fontSize = 16.5.sp,
          color = KimiTextWhite
        )
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Trigger Word Input Box with "0/20" counter
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(KimiSurfaceCard)
          .border(1.dp, KimiBorderSubtle, RoundedCornerShape(16.dp))
          .padding(horizontal = 18.dp),
        contentAlignment = Alignment.CenterStart
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Box(modifier = Modifier.weight(1f)) {
            if (triggerText.isEmpty()) {
              Text(
                text = "Enter a trigger word to qui...",
                style = MaterialTheme.typography.bodyLarge.copy(
                  color = KimiTextMuted,
                  fontSize = 15.sp
                ),
                maxLines = 1
              )
            }

            BasicTextField(
              value = triggerText,
              onValueChange = {
                if (it.length <= 20) triggerText = it
              },
              modifier = Modifier.fillMaxWidth(),
              textStyle = TextStyle(
                color = KimiTextWhite,
                fontSize = 15.sp
              ),
              cursorBrush = SolidColor(KimiBluePrimary),
              singleLine = true
            )
          }

          Text(
            text = "${triggerText.length}/20",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = KimiTextMuted,
              fontSize = 14.sp
            ),
            modifier = Modifier.padding(start = 8.dp)
          )
        }
      }
    }
  }
}
