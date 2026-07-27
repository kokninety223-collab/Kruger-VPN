package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.repository.AppThemeMode

data class VpnColors(
    val background: Color,
    val cardSurface: Color,
    val cardBorder: Color,
    val circleContainer: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textSubtitle: Color,
    val bottomNavBg: Color,
    val pillSelected: Color,
    val greenAction: Color,
    val isDark: Boolean
)

val DarkVpnColors = VpnColors(
    background = Color(0xFF0B1220),
    cardSurface = Color(0xFF162238),
    cardBorder = Color(0xFF233352),
    circleContainer = Color(0xFF223354),
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF94A3B8),
    textSubtitle = Color(0xFFCBD5E1),
    bottomNavBg = Color(0xFF0F182A),
    pillSelected = Color(0xFF223354),
    greenAction = Color(0xFF10B981),
    isDark = true
)

val LightVpnColors = VpnColors(
    background = Color(0xFFF1F5F9), // Light Slate 100
    cardSurface = Color(0xFFFFFFFF), // Pure White Card
    cardBorder = Color(0xFFCBD5E1), // Slate 300
    circleContainer = Color(0xFFE2E8F0), // Slate 200
    textPrimary = Color(0xFF0F172A), // Dark Slate 900
    textSecondary = Color(0xFF64748B), // Slate 500
    textSubtitle = Color(0xFF334155), // Slate 700
    bottomNavBg = Color(0xFFFFFFFF),
    pillSelected = Color(0xFFE2E8F0),
    greenAction = Color(0xFF059669), // Emerald 600
    isDark = false
)

val LocalVpnColors = staticCompositionLocalOf { DarkVpnColors }

val MaterialTheme.vpnColors: VpnColors
    @Composable
    get() = LocalVpnColors.current

@Composable
fun VpnControllerTheme(
    themeMode: AppThemeMode = AppThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val vpnColors = if (darkTheme) DarkVpnColors else LightVpnColors

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            background = vpnColors.background,
            surface = vpnColors.cardSurface,
            onBackground = vpnColors.textPrimary,
            onSurface = vpnColors.textPrimary,
            outline = vpnColors.cardBorder
        )
    } else {
        lightColorScheme(
            background = vpnColors.background,
            surface = vpnColors.cardSurface,
            onBackground = vpnColors.textPrimary,
            onSurface = vpnColors.textPrimary,
            outline = vpnColors.cardBorder
        )
    }

    CompositionLocalProvider(LocalVpnColors provides vpnColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
