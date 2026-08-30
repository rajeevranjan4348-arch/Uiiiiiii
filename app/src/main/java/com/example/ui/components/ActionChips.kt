package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PresentToAll
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Slideshow
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ActionChipType
import com.example.ui.theme.KimiBorderSubtle
import com.example.ui.theme.KimiPillBackground
import com.example.ui.theme.KimiPillBorder
import com.example.ui.theme.KimiTextPrimary
import com.example.ui.theme.KimiTextSecondary

@Composable
fun ActionChips(
  modifier: Modifier = Modifier,
  onChipClick: (ActionChipType) -> Unit
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState())
      .padding(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    ChipItem(
      label = "Slides",
      icon = Icons.Outlined.Slideshow,
      onClick = { onChipClick(ActionChipType.SLIDES) }
    )

    ChipItem(
      label = "Swarm",
      icon = Icons.Default.AccountTree,
      onClick = { onChipClick(ActionChipType.SWARM) }
    )

    ChipItem(
      label = "Websites",
      icon = Icons.Outlined.Folder,
      onClick = { onChipClick(ActionChipType.WEBSITES) }
    )
  }
}

@Composable
private fun ChipItem(
  label: String,
  icon: ImageVector,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .height(44.dp)
      .shadow(2.dp, RoundedCornerShape(22.dp), spotColor = Color.Black.copy(alpha = 0.3f))
      .clip(RoundedCornerShape(22.dp))
      .background(KimiPillBackground)
      .border(1.dp, KimiPillBorder, RoundedCornerShape(22.dp))
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = ripple(bounded = true),
        onClick = onClick
      )
      .padding(horizontal = 16.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = KimiTextSecondary,
        modifier = Modifier.size(17.dp)
      )
      Spacer(modifier = Modifier.width(7.dp))
      Text(
        text = label,
        style = MaterialTheme.typography.titleMedium.copy(
          fontSize = 14.5.sp,
          color = KimiTextPrimary
        )
      )
    }
  }
}
