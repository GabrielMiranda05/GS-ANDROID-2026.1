package br.com.windfyr.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = WhiteSurface,
    primaryContainer = GreenSurface,
    onPrimaryContainer = GreenDark,
    secondary = GreenAccent,
    onSecondary = WhiteSurface,
    background = GreenBackground,
    onBackground = GreenDark,
    surface = WhiteSurface,
    onSurface = GreenDark,
    surfaceVariant = GreenSurface,
    outline = GrayBorder
)

@Composable
fun WindfyrTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
