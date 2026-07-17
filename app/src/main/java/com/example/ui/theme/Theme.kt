package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = GoldPremium,
    secondary = VioletBrand,
    tertiary = BlueSoft,
    background = DarkBgMidnight,
    surface = DarkSurfaceCard,
    onPrimary = DarkBgBlack,
    onSecondary = PearlWhite,
    onTertiary = PearlWhite,
    onBackground = PearlWhite,
    onSurface = PearlWhite
  )

private val LightColorScheme = DarkColorScheme // Default to luxury dark even in light mode to preserve Monika's visual intent

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force dark theme to keep the premium atmospheric look
  dynamicColor: Boolean = false, // Disable dynamic colors to keep brand consistency
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
