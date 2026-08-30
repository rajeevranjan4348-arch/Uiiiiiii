package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActionChipType
import com.example.model.ChatMessage
import com.example.model.MessageSender
import com.example.model.ModelOption
import com.example.model.ThinkingEffort
import com.example.ui.components.ActionChips
import com.example.ui.components.AnimatedAIOrb
import com.example.ui.components.AttachmentBottomSheet
import com.example.ui.components.ChatComposer
import com.example.ui.components.ModelSelectorPopup
import com.example.ui.components.TopBar
import com.example.ui.theme.KimiBackground
import com.example.ui.theme.KimiBluePrimary
import com.example.ui.theme.KimiBorderHighlight
import com.example.ui.theme.KimiBorderSubtle
import com.example.ui.theme.KimiSurfaceCard
import com.example.ui.theme.KimiSurfaceCardSubtle
import com.example.ui.theme.KimiTextMuted
import com.example.ui.theme.KimiTextPrimary
import com.example.ui.theme.KimiTextSecondary
import com.example.ui.theme.KimiTextWhite

@Composable
fun MainChatScreen(
  modifier: Modifier = Modifier,
  userName: String = "Mamta Kumari",
  selectedModel: ModelOption,
  isModelPopupOpen: Boolean,
  thinkingEffort: ThinkingEffort,
  isAttachmentSheetOpen: Boolean,
  isMuted: Boolean,
  inputText: String,
  messages: List<ChatMessage>,
  onMenuClick: () -> Unit,
  onModelSelectorClick: () -> Unit,
  onSpeakerClick: () -> Unit,
  onSelectModel: (ModelOption) -> Unit,
  onCycleThinkingEffort: () -> Unit,
  onDismissModelPopup: () -> Unit,
  onToggleAttachmentSheet: (Boolean?) -> Unit,
  onAttachmentSelect: (String) -> Unit,
  onCommonPhrasesClick: () -> Unit,
  onVoiceClick: () -> Unit,
  onInputChange: (String) -> Unit,
  onSendMessage: () -> Unit,
  onChipClick: (ActionChipType) -> Unit
) {
  val listState = rememberLazyListState()

  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(KimiBackground)
      .statusBarsPadding()
      .navigationBarsPadding()
      .imePadding()
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Top Navigation Bar
      TopBar(
        selectedModel = selectedModel,
        isMuted = isMuted,
        onMenuClick = onMenuClick,
        onModelSelectorClick = onModelSelectorClick,
        onSpeakerClick = onSpeakerClick
      )

      // Main Content Area
      Box(
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth(),
        contentAlignment = Alignment.Center
      ) {
        if (messages.isEmpty()) {
          // EMPTY STATE (Screenshots 2 & 5)
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            // Glowing Cute AI Orb
            AnimatedAIOrb(
              size = 66.dp,
              isVoiceMode = false
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Greeting Text (Screenshot 5)
            Text(
              text = "Hi  $userName, what\nwould you like to do with Kimi\ntoday?",
              style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 21.sp,
                lineHeight = 30.sp,
                letterSpacing = (-0.2).sp,
                color = KimiTextWhite
              ),
              textAlign = TextAlign.Center
            )
          }

          // Floating mini waveform card on right when typing (Screenshot 2)
          if (inputText.isNotEmpty()) {
            Box(
              modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 18.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF2C2C32))
                .border(1.dp, KimiBorderSubtle, RoundedCornerShape(14.dp)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.GraphicEq,
                contentDescription = null,
                tint = KimiTextWhite,
                modifier = Modifier.size(24.dp)
              )
            }
          }
        } else {
          // ACTIVE CHAT LIST
          LazyColumn(
            state = listState,
            modifier = Modifier
              .fillMaxSize()
              .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            items(messages, key = { it.id }) { message ->
              ChatMessageBubble(message = message)
            }
          }
        }
      }

      // Bottom Action Chips (Visible in empty state or top of composer)
      if (messages.isEmpty()) {
        ActionChips(
          onChipClick = onChipClick,
          modifier = Modifier.padding(bottom = 6.dp)
        )
      }

      // Bottom Composer Input Bar
      ChatComposer(
        text = inputText,
        onTextChange = onInputChange,
        onSend = onSendMessage,
        onPlusClick = { onToggleAttachmentSheet(true) },
        onVoiceClick = onVoiceClick
      )
    }

    // Model Selector Floating Dropdown Popup
    if (isModelPopupOpen) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .clickable { onDismissModelPopup() }
      ) {
        ModelSelectorPopup(
          modifier = Modifier
            .padding(top = 58.dp, start = 16.dp)
            .align(Alignment.TopStart),
          isOpen = isModelPopupOpen,
          selectedModel = selectedModel,
          thinkingEffort = thinkingEffort,
          onSelectModel = onSelectModel,
          onCycleThinkingEffort = onCycleThinkingEffort,
          onDismiss = onDismissModelPopup
        )
      }
    }

    // Attachment Bottom Sheet
    AttachmentBottomSheet(
      isOpen = isAttachmentSheetOpen,
      onDismiss = { onToggleAttachmentSheet(false) },
      onAttachmentSelect = onAttachmentSelect,
      onCommonPhrasesClick = onCommonPhrasesClick,
      onCallClick = onVoiceClick
    )
  }
}

@Composable
private fun ChatMessageBubble(message: ChatMessage) {
  val isUser = message.sender == MessageSender.USER

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
  ) {
    if (!isUser) {
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
          .background(KimiBluePrimary.copy(alpha = 0.2f))
          .border(1.dp, KimiBluePrimary.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        AnimatedAIOrb(size = 28.dp)
      }
      Spacer(modifier = Modifier.width(8.dp))
    }

    Box(
      modifier = Modifier
        .clip(
          RoundedCornerShape(
            topStart = 18.dp,
            topEnd = 18.dp,
            bottomStart = if (isUser) 18.dp else 4.dp,
            bottomEnd = if (isUser) 4.dp else 18.dp
          )
        )
        .background(if (isUser) KimiSurfaceCardSubtle else KimiSurfaceCard)
        .border(
          1.dp,
          if (isUser) KimiBorderHighlight else KimiBorderSubtle,
          RoundedCornerShape(
            topStart = 18.dp,
            topEnd = 18.dp,
            bottomStart = if (isUser) 18.dp else 4.dp,
            bottomEnd = if (isUser) 4.dp else 18.dp
          )
        )
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      Column {
        if (message.attachmentName != null) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .padding(bottom = 6.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF2E313D))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "📎 ${message.attachmentName}",
              style = MaterialTheme.typography.bodySmall.copy(
                color = KimiTextWhite,
                fontWeight = FontWeight.Medium
              )
            )
          }
        }

        if (message.text.isEmpty() && message.isStreaming) {
          TypingIndicatorDots()
        } else {
          Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
          ) {
            Text(
              text = message.text,
              style = MaterialTheme.typography.bodyLarge.copy(
                color = KimiTextPrimary,
                fontSize = 15.sp,
                lineHeight = 22.sp
              )
            )
            if (message.isStreaming) {
              Spacer(modifier = Modifier.width(4.dp))
              StreamingCursor()
            }
          }
        }
      }
    }
  }
}

@Composable
private fun TypingIndicatorDots(modifier: Modifier = Modifier) {
  val infiniteTransition = rememberInfiniteTransition(label = "TypingDots")

  val dot1OffsetY by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = -7f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 480, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "dot1"
  )
  val dot2OffsetY by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = -7f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 480, delayMillis = 160, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "dot2"
  )
  val dot3OffsetY by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = -7f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 480, delayMillis = 320, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "dot3"
  )

  Row(
    horizontalArrangement = Arrangement.spacedBy(7.dp),
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier.padding(vertical = 6.dp, horizontal = 4.dp)
  ) {
    val offsets = listOf(dot1OffsetY, dot2OffsetY, dot3OffsetY)
    offsets.forEach { offsetY ->
      val activeRatio = (-offsetY / 7f).coerceIn(0f, 1f)
      val alpha = 0.45f + 0.55f * activeRatio
      val scale = 0.85f + 0.30f * activeRatio

      Box(
        modifier = Modifier
          .offset(y = offsetY.dp)
          .size((8 * scale).dp)
          .clip(CircleShape)
          .background(KimiBluePrimary.copy(alpha = alpha))
      )
    }
  }
}

@Composable
private fun StreamingCursor() {
  val infiniteTransition = rememberInfiniteTransition(label = "CursorBlink")
  val alpha by infiniteTransition.animateFloat(
    initialValue = 0.2f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 500, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "cursorAlpha"
  )

  Box(
    modifier = Modifier
      .padding(bottom = 3.dp)
      .size(width = 7.dp, height = 15.dp)
      .clip(RoundedCornerShape(2.dp))
      .background(KimiBluePrimary.copy(alpha = alpha))
  )
}
