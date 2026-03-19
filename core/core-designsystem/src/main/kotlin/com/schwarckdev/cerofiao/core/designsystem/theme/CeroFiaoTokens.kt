package com.schwarckdev.cerofiao.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Extended CeroFiao design tokens beyond Material 3.
 * Provides glassmorphism surfaces, semantic colors, and brand gradients.
 */
@Immutable
data class CeroFiaoColorTokens(
    // Backgrounds
    val bg: Color,
    val bgSecondary: Color,
    val bgModal: Color,

    // Glassmorphism surfaces
    val surface: Color,
    val surfaceBorder: Color,
    val surfaceHover: Color,

    // Text hierarchy
    val text: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textMuted: Color,
    val textFaint: Color,
    val textGhost: Color,

    // Navigation bar
    val navBg: Color,
    val navBorder: Color,

    // Dividers & inputs
    val divider: Color,
    val inputBg: Color,
    val inputBorder: Color,
    val placeholder: Color,

    // Components
    val pillBg: Color,
    val progressBg: Color,
    val iconBg: Color,
    val handleBg: Color,

    // Semantic
    val success: Color,
    val danger: Color,
    val expense: Color,

    // Chart
    val chartLabel: Color,
    val tooltipBg: Color,
    val tooltipBorder: Color,
    val dotStroke: Color,
)

val DarkCeroFiaoTokens = CeroFiaoColorTokens(
    bg = Color(0xFF050505),
    bgSecondary = Color(0xFF0A0A0A),
    bgModal = Color(0xFF0A0A0A),

    surface = Color(0x0AFFFFFF),         // white 4%
    surfaceBorder = Color(0x14FFFFFF),   // white 8%
    surfaceHover = Color(0x0FFFFFFF),    // white 6%

    text = Color(0xFFFFFFFF),
    textSecondary = Color(0x8CFFFFFF),   // white 55%
    textTertiary = Color(0x4DFFFFFF),    // white 30%
    textMuted = Color(0x40FFFFFF),       // white 25%
    textFaint = Color(0x26FFFFFF),       // white 15%
    textGhost = Color(0x1AFFFFFF),       // white 10%

    navBg = Color(0xCC0A0A0A),           // 80% opacity
    navBorder = Color(0x14FFFFFF),       // white 8%

    divider = Color(0x0AFFFFFF),         // white 4%
    inputBg = Color(0x0AFFFFFF),         // white 4%
    inputBorder = Color(0x0FFFFFFF),     // white 6%
    placeholder = Color(0x26FFFFFF),     // white 15%

    pillBg = Color(0x0FFFFFFF),          // white 6%
    progressBg = Color(0x0FFFFFFF),      // white 6%
    iconBg = Color(0x0FFFFFFF),          // white 6%
    handleBg = Color(0x1AFFFFFF),        // white 10%

    success = Color(0xFF00FF66),
    danger = Color(0xFFFF4433),
    expense = Color(0xB3FFFFFF),         // white 70%

    chartLabel = Color(0x40FFFFFF),       // white 25%
    tooltipBg = Color(0xFF1A1A1A),
    tooltipBorder = Color(0x14FFFFFF),   // white 8%
    dotStroke = Color(0xFF050505),
)

val LightCeroFiaoTokens = CeroFiaoColorTokens(
    bg = Color(0xFFF2F2F7),
    bgSecondary = Color(0xFFFFFFFF),
    bgModal = Color(0xFFFFFFFF),

    surface = Color(0xCCFFFFFF),         // white 80%
    surfaceBorder = Color(0x0F000000),   // black 6%
    surfaceHover = Color(0x0A000000),    // black 4%

    text = Color(0xFF1C1C1E),
    textSecondary = Color(0x8C000000),   // black 55%
    textTertiary = Color(0x59000000),    // black 35%
    textMuted = Color(0x40000000),       // black 25%
    textFaint = Color(0x1F000000),       // black 12%
    textGhost = Color(0x0F000000),       // black 6%

    navBg = Color(0xD9FFFFFF),           // 85% opacity
    navBorder = Color(0x0F000000),       // black 6%

    divider = Color(0x0A000000),         // black 4%
    inputBg = Color(0x08000000),         // black 3%
    inputBorder = Color(0x0F000000),     // black 6%
    placeholder = Color(0x33000000),     // black 20%

    pillBg = Color(0x0A000000),          // black 4%
    progressBg = Color(0x0F000000),      // black 6%
    iconBg = Color(0x0A000000),          // black 4%
    handleBg = Color(0x1A000000),        // black 10%

    success = Color(0xFF00CC52),
    danger = Color(0xFFFF3B30),
    expense = Color(0xA6000000),         // black 65%

    chartLabel = Color(0x4D000000),       // black 30%
    tooltipBg = Color(0xFFFFFFFF),
    tooltipBorder = Color(0x14000000),   // black 8%
    dotStroke = Color(0xFFF2F2F7),
)

val LocalCeroFiaoTokens = staticCompositionLocalOf { DarkCeroFiaoTokens }

/**
 * Brand gradient brush (purple → orange)
 */
val BrandGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF8A2BE2), Color(0xFFFF6B00)),
)

/**
 * Success gradient brush (green)
 */
val SuccessGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF00FF66), Color(0xFF00CC52)),
)

/**
 * Danger gradient brush (red)
 */
val DangerGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFFF4433), Color(0xFFCC2211)),
)

// Design system constants
object CeroFiaoShapes {
    val CardRadius = 24.dp
    val SmallCardRadius = 16.dp
    val ButtonRadius = 28.dp // rounded-full equivalent
    val ChipRadius = 20.dp
    val BottomSheetRadius = 32.dp
    val IconRadius = 14.dp
}
