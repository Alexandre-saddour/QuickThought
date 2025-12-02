package com.quickthought.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryIndigo,
    onPrimary = TextOnPrimary,
    primaryContainer = PrimaryIndigoLight,
    onPrimaryContainer = TextPrimary,
    secondary = AccentTeal,
    onSecondary = TextOnPrimary,
    secondaryContainer = AccentTealLight,
    onSecondaryContainer = TextPrimary,
    tertiary = TaskColor,
    onTertiary = TextPrimary,
    error = ErrorRuby,
    onError = TextOnPrimary,
    errorContainer = ErrorRubyLight,
    onErrorContainer = TextPrimary,
    background = Background,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceLightGray,
    onSurfaceVariant = TextSecondary,
    outline = Divider,
    outlineVariant = BorderLight
)

@Composable
fun QuickThoughtTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = QuickThoughtTypography,
        content = content
    )
}
