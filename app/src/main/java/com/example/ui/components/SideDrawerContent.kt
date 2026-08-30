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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.CrisisAlert
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.Grain
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Schedule
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatHistoryItem
import com.example.ui.theme.KimiBackgroundElevated
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
fun SideDrawerContent(
  modifier: Modifier = Modifier,
  userName: String = "Mamta Kumari",
  historyItems: List<ChatHistoryItem>,
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  onNewChat: () -> Unit,
  onHistoryItemClick: (ChatHistoryItem) -> Unit,
  onUpgradePlanClick: () -> Unit = {},
  onScheduledTasksClick: () -> Unit = {},
  onKimiClawClick: () -> Unit = {},
  onInviteToEarnClick: () -> Unit = {}
) {
  Column(
    modifier = modifier
      .fillMaxHeight()
      .background(KimiBackgroundElevated)
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(horizontal = 20.dp, vertical = 12.dp)
  ) {
    // TOP PROFILE ROW
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Circular Profile Picture
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(CircleShape)
          .background(
            Brush.linearGradient(
              colors = listOf(Color(0xFFE29578), Color(0xFF006D77))
            )
          )
          .border(1.5.dp, KimiBorderHighlight, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "MK",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Color.White
          )
        )
      }

      Spacer(modifier = Modifier.width(14.dp))

      Text(
        text = userName,
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.SemiBold,
          fontSize = 19.sp,
          color = KimiTextWhite
        ),
        modifier = Modifier.weight(1f)
      )

      // Circular Add / New Chat Button
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(KimiIconCircle)
          .border(1.dp, KimiIconCircleBorder, CircleShape)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = true),
            onClick = onNewChat
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "New Chat",
          tint = KimiTextWhite,
          modifier = Modifier.size(22.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // NAVIGATION ITEMS
    DrawerNavItem(
      icon = Icons.Outlined.MusicNote,
      title = "Upgrade Plan",
      onClick = onUpgradePlanClick
    )

    Spacer(modifier = Modifier.height(6.dp))

    DrawerNavItem(
      icon = Icons.Outlined.Alarm,
      title = "Scheduled tasks",
      onClick = onScheduledTasksClick
    )

    Spacer(modifier = Modifier.height(6.dp))

    DrawerNavItem(
      icon = Icons.Outlined.Grain,
      title = "Kimi Claw",
      onClick = onKimiClawClick
    )

    Spacer(modifier = Modifier.height(6.dp))

    // Invite to Earn with blue badge "Get K3 Credits"
    DrawerNavItem(
      icon = Icons.Default.Handshake,
      title = "Invite to Earn",
      badgeText = "Get K3 Credits",
      onClick = onInviteToEarnClick
    )

    Spacer(modifier = Modifier.height(28.dp))

    // CHAT HISTORY HEADER
    Text(
      text = "Chat history",
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        color = KimiTextMuted
      ),
      modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
    )

    Spacer(modifier = Modifier.height(8.dp))

    // CHAT HISTORY LIST
    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      val filtered = historyItems.filter {
        searchQuery.isBlank() || it.title.contains(searchQuery, ignoreCase = true)
      }

      items(filtered, key = { it.id }) { item ->
        HistoryRowItem(
          item = item,
          onClick = { onHistoryItemClick(item) }
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // BOTTOM SEARCH AND SCANNER ROW
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Search Bar
      Box(
        modifier = Modifier
          .weight(1f)
          .height(52.dp)
          .clip(RoundedCornerShape(26.dp))
          .background(KimiSurfaceCard)
          .border(1.dp, KimiBorderSubtle, RoundedCornerShape(26.dp))
          .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = KimiTextMuted,
            modifier = Modifier.size(20.dp)
          )

          Spacer(modifier = Modifier.width(10.dp))

          if (searchQuery.isEmpty()) {
            Text(
              text = "Search",
              style = MaterialTheme.typography.bodyLarge.copy(
                color = KimiTextMuted,
                fontSize = 15.sp
              )
            )
          }

          BasicTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
              color = KimiTextWhite,
              fontSize = 15.sp
            ),
            cursorBrush = SolidColor(KimiBluePrimary),
            singleLine = true
          )
        }
      }

      // Scanner Utility Button
      Box(
        modifier = Modifier
          .size(52.dp)
          .clip(CircleShape)
          .background(KimiSurfaceCard)
          .border(1.dp, KimiBorderSubtle, CircleShape)
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = true),
            onClick = { /* Scanner action */ }
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.CenterFocusWeak,
          contentDescription = "Scanner",
          tint = KimiTextWhite,
          modifier = Modifier.size(22.dp)
        )
      }
    }
  }
}

@Composable
private fun DrawerNavItem(
  icon: ImageVector,
  title: String,
  badgeText: String? = null,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(bounded = true),
        onClick = onClick
      )
      .padding(horizontal = 6.dp, vertical = 10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = title,
      tint = KimiTextWhite,
      modifier = Modifier.size(22.dp)
    )

    Spacer(modifier = Modifier.width(16.dp))

    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 16.5.sp,
        color = KimiTextWhite
      )
    )

    if (badgeText != null) {
      Spacer(modifier = Modifier.width(10.dp))
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(Color(0xFF132F5C))
          .padding(horizontal = 8.dp, vertical = 3.dp)
      ) {
        Text(
          text = badgeText,
          style = MaterialTheme.typography.labelSmall.copy(
            color = KimiBluePrimary,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.SemiBold
          )
        )
      }
    }
  }
}

@Composable
private fun HistoryRowItem(
  item: ChatHistoryItem,
  onClick: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(bounded = true),
        onClick = onClick
      )
      .padding(horizontal = 4.dp, vertical = 4.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = item.title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Medium,
          fontSize = 16.5.sp,
          color = KimiTextWhite
        )
      )

      if (item.hasUnreadDot) {
        Box(
          modifier = Modifier
            .size(7.dp)
            .clip(CircleShape)
            .background(KimiBluePrimary)
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Thumbnail Preview Card (Screenshot 1)
    Box(
      modifier = Modifier
        .width(68.dp)
        .height(48.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(
          when (item.previewTheme) {
            1 -> Brush.verticalGradient(
              colors = listOf(Color(0xFF3B1E6D), Color(0xFF121422))
            )
            2 -> Brush.verticalGradient(
              colors = listOf(Color(0xFF1E3C72), Color(0xFF10121C))
            )
            else -> Brush.verticalGradient(
              colors = listOf(Color(0xFF262832), Color(0xFF15161C))
            )
          }
        )
        .border(1.dp, KimiBorderSubtle, RoundedCornerShape(8.dp))
        .padding(5.dp)
    ) {
      // Mini UI mockup lines inside thumbnail
      Column(
        verticalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(2.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(modifier = Modifier.size(5.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.5f)))
          Box(modifier = Modifier.height(4.dp).weight(1f).clip(RoundedCornerShape(2.dp)).background(Color.White.copy(alpha = 0.3f)))
        }
        Box(modifier = Modifier.height(3.dp).fillMaxWidth(0.7f).clip(RoundedCornerShape(2.dp)).background(Color.White.copy(alpha = 0.2f)))
        Box(modifier = Modifier.height(3.dp).fillMaxWidth(0.9f).clip(RoundedCornerShape(2.dp)).background(Color.White.copy(alpha = 0.2f)))
      }
    }
  }
}
