package com.schwarckdev.cerofiao.core.designsystem.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ═══════════════════════════════════════════════════════════════
// CeroFiao Design Tokens — Mapped from Figma Export + LionFitness
// ═══════════════════════════════════════════════════════════════

data class CeroFiaoColors(
    // Primary Colors
    val Primary: Color,
    val OnPrimary: Color,
    
    // Background Colors
    val Background: Color,
    val Surface: Color,
    val SurfaceVariant: Color,
    
    // Navigation Colors
    val NavBackground: Color,
    val NavBackgroundTransparent: Color,
    val ActiveItemBackground: Color,
    val InactiveColor: Color,
    
    // Text Colors
    val TextPrimary: Color,
    val TextSecondary: Color,
    val TextOnDark: Color,
    
    // Accents & Foreground from Figma
    val Foreground: Color,
    val GradientAccent: Color,
    val SecondaryAccent: Color,
    
    // Accent Colors (Financial Context)
    val IncomeColor: Color,
    val ExpenseColor: Color,
    val InternalTransferColor: Color,
    
    val AccentGreen: Color,
    val AccentBlue: Color,
    val AccentOrange: Color,
    val AccentRed: Color,
    val AccentPurple: Color,
    val AccentUser: Color,
    
    // Card Colors
    val CardBackground: Color,
    val CardBorder: Color,
    
    // Error Color
    val Error: Color,
    
    // Shadow Colors
    val ShadowColor: Color,
    val ShadowColorLight: Color,
    
    // Glassmorphism
    val GlassBackground: Color,
    val GlassBackgroundDark: Color,
    val GlassBorder: Color,
    
    // Unified Menu Background
    val fondoMenus: Color,

    // Currency Colors
    val UsdColor: Color,
    val BsColor: Color,
    val UsdtColor: Color,
    
    // Category Colors
    val CategoryFood: Color,
    val CategoryCoffee: Color,
    val CategoryTransport: Color,
    val CategoryHome: Color,
    val CategoryServices: Color,
    val CategoryEntertainment: Color,
    val CategoryTech: Color,
    val CategoryWork: Color,
    val CategoryHealth: Color,
    val CategoryOther: Color
) {
    // ── Backward-compatible aliases (only where names don't clash on JVM) ──
    val income: Color get() = IncomeColor
    val expense: Color get() = ExpenseColor
    val incomeSoft: Color get() = IncomeColor.copy(alpha = 0.12f)
    val expenseSoft: Color get() = ExpenseColor.copy(alpha = 0.12f)
}

val LightCeroFiaoColors = CeroFiaoColors(
    Primary = Color(0xFF00B8D4),
    OnPrimary = Color(0xFFFFFFFF),
    
    Background = Color(0xfff1f2f3),
    Surface = Color(0xFFFFFFFF),
    SurfaceVariant = Color(0xFFE8E8EA),
    
    NavBackground = Color(0xFFFFFFFF),
    NavBackgroundTransparent = Color(0x99FFFFFF),
    ActiveItemBackground = Color(0xffbfbfbf),
    InactiveColor = Color(0xFF99999D),
    
    TextPrimary = Color(0xFF000000),
    TextSecondary = Color(0xFF99999D),
    TextOnDark = Color(0xFFFFFFFF),
    
    Foreground = Color(0xFFFCFCFF),
    GradientAccent = Color(0xFF00B8D4),
    SecondaryAccent = Color(0xFFFFCB0C),
    
    IncomeColor = Color(0xFF34C759),
    ExpenseColor = Color(0xFFFF3B30),
    InternalTransferColor = Color(0xFF8E8E93),
    
    AccentGreen = Color(0xFF4CAF50),
    AccentBlue = Color(0xFF387AFF),
    AccentOrange = Color(0xFFFF9800),
    AccentRed = Color(0xFFF44336),
    AccentPurple = Color(0xFF9C27B0),
    AccentUser = Color(0xFF00BCD4),
    
    CardBackground = Color(0xFFFFFFFF),
    CardBorder = Color(0x0F000000),
    
    Error = Color(0xFFFF3B30),
    
    ShadowColor = Color(0x40000000),
    ShadowColorLight = Color(0x1A000000),
    
    GlassBackground = Color(0xB8FFFFFF),
    GlassBackgroundDark = Color(0x80FFFFFF),
    GlassBorder = Color(0x0F000000),
    
    fondoMenus = Color(0xFFFFFFFF),
    
    UsdColor = Color(0xFF22C55E),
    BsColor = Color(0xFFF97316),
    UsdtColor = Color(0xFFEAB308),
    
    CategoryFood = Color(0xFFFF9F0A),
    CategoryCoffee = Color(0xFFFF6482),
    CategoryTransport = Color(0xFF5E5CE6),
    CategoryHome = Color(0xFF64D2FF),
    CategoryServices = Color(0xFFBF5AF2),
    CategoryEntertainment = Color(0xFFFF375F),
    CategoryTech = Color(0xFF30D158),
    CategoryWork = Color(0xFF00B8D4),
    CategoryHealth = Color(0xFFFF453A),
    CategoryOther = Color(0xFF98989D)
)

val DarkCeroFiaoColors = CeroFiaoColors(
    Primary = Color(0xFF00E5FF),
    OnPrimary = Color(0xFF000000),
    
    Background = Color(0xFF000000),
    Surface = Color(0xFF17171A),
    SurfaceVariant = Color(0xFF17171A),
    
    NavBackground = Color(0xFF000000),
    NavBackgroundTransparent = Color(0xCC000000),
    ActiveItemBackground = Color(0xFF17171A),
    InactiveColor = Color(0xFF9A9A9E),
    
    TextPrimary = Color(0xFFFFFFFF),
    TextSecondary = Color(0xFF9A9A9E),
    TextOnDark = Color(0xFFFFFFFF),
    
    Foreground = Color(0xFF2E2E30),
    GradientAccent = Color(0xFF00E5FF),
    SecondaryAccent = Color(0xFFFFCB0C),
    
    IncomeColor = Color(0xFF30D158),
    ExpenseColor = Color(0xFFFF453A),
    InternalTransferColor = Color(0xFF636366),
    
    AccentGreen = Color(0xFF66BB6A),
    AccentBlue = Color(0xFF387AFF),
    AccentOrange = Color(0xFFFFCA28),
    AccentRed = Color(0xFFEF5350),
    AccentPurple = Color(0xFFBA68C8),
    AccentUser = Color(0xFF00BCD4),
    
    CardBackground = Color(0xFF17171A),
    CardBorder = Color(0x26FFFFFF), // 15% OutlineDark
    
    Error = Color(0xFFFF453A),
    
    ShadowColor = Color(0x80000000),
    ShadowColorLight = Color(0x40000000),
    
    GlassBackground = Color(0x0DFFFFFF),
    GlassBackgroundDark = Color(0x80000000),
    GlassBorder = Color(0x26FFFFFF),
    
    fondoMenus = Color(0xFF17171A),
    
    UsdColor = Color(0xFF22C55E),
    BsColor = Color(0xFFF97316),
    UsdtColor = Color(0xFFEAB308),
    
    CategoryFood = Color(0xFFFF9F0A),
    CategoryCoffee = Color(0xFFFF6482),
    CategoryTransport = Color(0xFF5E5CE6),
    CategoryHome = Color(0xFF64D2FF),
    CategoryServices = Color(0xFFBF5AF2),
    CategoryEntertainment = Color(0xFFFF375F),
    CategoryTech = Color(0xFF30D158),
    CategoryWork = Color(0xFF00B8D4),
    CategoryHealth = Color(0xFFFF453A),
    CategoryOther = Color(0xFF98989D)
)

fun Color.withAlpha(alpha: Float): Color = this.copy(alpha = alpha)

data class CeroFiaoEffects(
    val blurNone: Dp,
    val blurLow: Dp,
    val blurMedium: Dp,
    val blurHigh: Dp,
    
    val alphaLow: Float,
    val alphaMedium: Float,
    val alphaHigh: Float
)

val LightCeroFiaoEffects = CeroFiaoEffects(
    blurNone = 0.dp,
    blurLow = 14.dp,
    blurMedium = 30.dp,
    blurHigh = 50.dp,
    
    alphaLow = 0.15f,
    alphaMedium = 0.60f,
    alphaHigh = 0.85f
)

val DarkCeroFiaoEffects = LightCeroFiaoEffects

// ── Top-level re-exports for backward compatibility ──
// These allow `import com.schwarckdev.cerofiao.core.designsystem.theme.UsdColor` to keep working.
val UsdColor: Color = Color(0xFF22C55E)
val BsColor: Color = Color(0xFFF97316)
val UsdtColor: Color = Color(0xFFEAB308)

val CategoryFood: Color = Color(0xFFFF9F0A)
val CategoryCoffee: Color = Color(0xFFFF6482)
val CategoryTransport: Color = Color(0xFF5E5CE6)
val CategoryHome: Color = Color(0xFF64D2FF)
val CategoryServices: Color = Color(0xFFBF5AF2)
val CategoryEntertainment: Color = Color(0xFFFF375F)
val CategoryTech: Color = Color(0xFF30D158)
val CategoryWork: Color = Color(0xFF00B8D4)
val CategoryHealth: Color = Color(0xFFFF453A)
val CategoryOther: Color = Color(0xFF98989D)

// ── Old CeroFiaoExtendedColors alias for backward compat ──
typealias CeroFiaoExtendedColors = CeroFiaoColors

// ── Backward-compat top-level vals used by core-ui ──
val IncomeGreen: Color = Color(0xFF34C759)
val ExpenseRed: Color = Color(0xFFFF3B30)
val TransferBlue: Color = Color(0xFF8E8E93)

object CeroFiaoShapes {
    val CardRadius = 20.dp
    val SmallCardRadius = 16.dp
    val ButtonRadius = 25.dp
    val ChipRadius = 16.dp
    val BottomSheetRadius = 28.dp
    val IconRadius = 14.dp
}

// ── Gradient Brushes ──
val BrandGradient = Brush.linearGradient(listOf(Color(0xFF8A2BE2), Color(0xFFFF6B00)))
val DangerGradient = Brush.linearGradient(listOf(Color(0xFFFF4433), Color(0xFFCC2211)))
val SuccessGradient = Brush.linearGradient(listOf(Color(0xFF00FF66), Color(0xFF00CC52)))
val ExpenseGradient = Brush.horizontalGradient(listOf(Color(0xFF8A2BE2), Color(0xFFFF6B00)))
val TransferGradient = Brush.horizontalGradient(listOf(Color(0xFF66A1F3), Color(0xFF22C9A6)))
val IncomeGradient = Brush.horizontalGradient(listOf(Color(0x8085F366), Color(0x8022C9A6)))

// ── Account Type Badge Colors ──
object AccountBadgeColors {
    val CryptoBg = Color(0xFFFBFF00).copy(alpha = 0.2f)
    val CryptoText = Color(0xFFAAA700)
    val BankBg = Color(0xFF00EAFF).copy(alpha = 0.2f)
    val BankText = Color(0xFF009CAA)
    val WalletBg = Color(0xFFFF6E84).copy(alpha = 0.1f)
    val WalletText = Color(0xFFD73357)
    val CashBg = Color(0xFF00FF51).copy(alpha = 0.1f)
    val CashText = Color(0xFF0CA523)
}

// ── Exchange Rate Colors ──
object RateColors {
    val BcvGreen = Color(0xFF00BB89)
    val ParallelTeal = Color(0xFF00819A)
}
