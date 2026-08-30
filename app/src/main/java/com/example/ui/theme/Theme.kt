package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val KimiDarkColorScheme = darkColorScheme(
  primary = KimiBluePrimary,
  onPrimary = KimiTextWhite,
  primaryContainer = KimiSurfaceCard,
  onPrimaryContainer = KimiTextPrimary,
  secondary = KimiBlueLight,
  onSecondary = KimiBackground,
  secondaryContainer = KimiPillBackground,
  onSecondaryContainer = KimiTextSecondary,
  tertiary = KimiGreenActive,
  onTertiary = KimiTextWhite,
  background = KimiBackground,
  onBackground = KimiTextPrimary,
  surface = KimiSurfaceCard,
  onSurface = KimiTextPrimary,
  surfaceVariant = KimiSurfaceCardSubtle,
  onSurfaceVariant = KimiTextSecondary,
  outline = KimiBorderSubtle,
  outlineVariant = KimiBorderHighlight,
  error = KimiRedEndCall,
  onError = KimiTextWhite
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit
) {
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      window.statusBarColor = KimiBackground.toArgb()
      window.navigationBarColor = KimiBackground.toArgb()
      val insetsController = WindowCompat.getInsetsController(window, view)
      insetsController.isAppearanceLightStatusBars = false
      insetsController.isAppearanceLightNavigationBars = false
    }
  }

  MaterialTheme(
    colorScheme = KimiDarkColorScheme,
    typography = Typography,
    content = content
  )
}
