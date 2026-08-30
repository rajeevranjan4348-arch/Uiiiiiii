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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.ElectricBolt
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KimiBackgroundElevated
import com.example.ui.theme.KimiBorderHighlight
import com.example.ui.theme.KimiBorderSubtle
import com.example.ui.theme.KimiSurfaceCard
import com.example.ui.theme.KimiSurfaceCardSubtle
import com.example.ui.theme.KimiTextMuted
import com.example.ui.theme.KimiTextPrimary
import com.example.ui.theme.KimiTextSecondary
import com.example.ui.theme.KimiTextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentBottomSheet(
  isOpen: Boolean,
  onDismiss: () -> Unit,
  onAttachmentSelect: (String) -> Unit,
  onCommonPhrasesClick: () -> Unit,
  onCallClick: () -> Unit
) {
  if (!isOpen) return

  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = KimiBackgroundElevated,
    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
    dragHandle = {
      Box(
        modifier = Modifier
          .padding(top = 12.dp, bottom = 8.dp)
          .width(38.dp)
          .height(4.dp)
          .clip(CircleShape)
          .background(KimiTextMuted.copy(alpha = 0.6f))
      )
    }
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp)
        .padding(bottom = 24.dp)
        .navigationBarsPadding()
    ) {
      Spacer(modifier = Modifier.height(8.dp))

      // Row 1: Camera, Photos, Local file (3 large square cards)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        AttachmentCard(
          modifier = Modifier.weight(1f),
          icon = Icons.Default.CameraAlt,
          label = "Camera",
          onClick = { onAttachmentSelect("Photo Capture") }
        )
        AttachmentCard(
          modifier = Modifier.weight(1f),
          icon = Icons.Default.PhotoLibrary,
          label = "Photos",
          onClick = { onAttachmentSelect("Gallery Image") }
        )
        AttachmentCard(
          modifier = Modifier.weight(1f),
          icon = Icons.Default.UploadFile,
          label = "Local file",
          onClick = { onAttachmentSelect("Document.pdf") }
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Row 2: WeChat files, Call, Common phrases (Screenshot 4)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        AttachmentCard(
          modifier = Modifier.weight(1f),
          icon = Icons.AutoMirrored.Filled.Chat,
          label = "WeChat\nfiles",
          onClick = { onAttachmentSelect("Chat File") }
        )
        AttachmentCard(
          modifier = Modifier.weight(1f),
          icon = Icons.Default.Call,
          label = "Call",
          onClick = onCallClick
        )
        AttachmentCard(
          modifier = Modifier.weight(1f),
          icon = Icons.Default.ViewInAr,
          label = "Common\nphrases",
          onClick = onCommonPhrasesClick
        )
      }

      Spacer(modifier = Modifier.height(28.dp))

      // PLUGIN SECTION
      ActionRowItem(
        icon = Icons.Outlined.Power,
        title = "Plugins",
        description = "Connect apps and databases\nto automate actions for you",
        onClick = { onAttachmentSelect("Plugin: GitHub + Notion") }
      )

      Spacer(modifier = Modifier.height(20.dp))

      // SKILLS SECTION
      ActionRowItem(
        icon = Icons.Outlined.Assessment,
        title = "Skills",
        description = "Reuse specialized skills to\nhandle specific tasks reliably",
        onClick = { onAttachmentSelect("Skill: Code Auditor") }
      )

      Spacer(modifier = Modifier.height(20.dp))

      // WEB SEARCH SECTION
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = true),
            onClick = { onAttachmentSelect("Web Search Enabled") }
          )
          .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Outlined.Language,
          contentDescription = "Web Search",
          tint = KimiTextWhite,
          modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
          text = "Web search",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp,
            color = KimiTextWhite
          )
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Auto",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontSize = 15.sp,
              color = KimiTextSecondary
            )
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = KimiTextMuted,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun AttachmentCard(
  modifier: Modifier = Modifier,
  icon: ImageVector,
  label: String,
  onClick: () -> Unit
) {
  Box(
    modifier = modifier
      .height(100.dp)
      .clip(RoundedCornerShape(20.dp))
      .background(KimiSurfaceCard)
      .border(1.dp, KimiBorderSubtle, RoundedCornerShape(20.dp))
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(bounded = true),
        onClick = onClick
      )
      .padding(12.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = KimiTextWhite,
        modifier = Modifier.size(28.dp)
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = label,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Medium,
          fontSize = 13.5.sp,
          lineHeight = 16.sp,
          color = KimiTextPrimary
        ),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
      )
    }
  }
}

@Composable
private fun ActionRowItem(
  icon: ImageVector,
  title: String,
  description: String,
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
      .padding(vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = title,
      tint = KimiTextWhite,
      modifier = Modifier.size(24.dp)
    )

    Spacer(modifier = Modifier.width(16.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.SemiBold,
          fontSize = 17.sp,
          color = KimiTextWhite
        )
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium.copy(
          fontSize = 13.5.sp,
          lineHeight = 18.sp,
          color = KimiTextSecondary
        )
      )
    }

    Icon(
      imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
      contentDescription = null,
      tint = KimiTextMuted,
      modifier = Modifier.size(22.dp)
    )
  }
}
