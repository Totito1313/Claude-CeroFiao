package com.schwarckdev.cerofiao.core.designsystem.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ═══════════════════════════════════════════════════════════════
// Theme Preferences Models — Serializable choices for Settings
// ═══════════════════════════════════════════════════════════════

/** Theme mode selection */
enum class ThemeMode(val label: String) {
    Auto("Auto"),
    Light("Claro"),
    Dark("Oscuro")
}

/** Available title font options */
enum class FontOption(
    val label: String,
    val id: String
) {
    Anton("Anton", "anton"),
    BebasNeue("Bebas Neue", "bebas"),
    Barlow("Barlow", "barlow"),
    Asap("Asap", "asap"),
    Inter("Inter", "inter");

    companion object {
        fun fromId(id: String): FontOption =
            entries.find { it.id == id } ?: Anton
    }

    fun toFontFamily(): FontFamily = when (this) {
        Anton -> AntonFontFamily
        BebasNeue -> BebasNeueFontFamily
        Barlow -> BarlowFontFamily
        Asap -> AsapFontFamily
        Inter -> InterFontFamily
    }
}

/** Available body font options */
enum class BodyFontOption(
    val label: String,
    val id: String
) {
    OneUiSans("One UI Sans", "oneui"),
    Quicksand("Quicksand", "quicksand"),
    Barlow("Barlow", "barlow"),
    Inter("Inter", "inter");

    companion object {
        fun fromId(id: String): BodyFontOption =
            entries.find { it.id == id } ?: OneUiSans
    }

    fun toFontFamily(): FontFamily = when (this) {
        OneUiSans -> OneUiSansFontFamily
        Quicksand -> QuicksandFontFamily
        Barlow -> BarlowFontFamily
        Inter -> InterFontFamily
    }
}

/** Preset accent colors for the color picker */
enum class AccentPreset(
    val label: String,
    val lightColor: Color,
    val darkColor: Color,
    val id: String
) {
    Cyan("Cyan", Color(0xFF00B8D4), Color(0xFF00E5FF), "cyan"),
    Blue("Azul", Color(0xFF387AFF), Color(0xFF387AFF), "blue"),
    Purple("Púrpura", Color(0xFF9C27B0), Color(0xFFBA68C8), "purple"),
    Green("Verde", Color(0xFF4CAF50), Color(0xFF66BB6A), "green"),
    Orange("Naranja", Color(0xFFFF9800), Color(0xFFFFCA28), "orange"),
    Red("Rojo", Color(0xFFF44336), Color(0xFFEF5350), "red"),
    Pink("Rosa", Color(0xFFEC407A), Color(0xFFF48FB1), "pink"),
    Yellow("Amarillo", Color(0xFFFFCB0C), Color(0xFFFFD54F), "yellow");

    companion object {
        fun fromId(id: String): AccentPreset =
            entries.find { it.id == id } ?: Cyan
    }
}

/** Global shadow configuration */
data class ShadowConfig(
    val enabled: Boolean = true,
    val alpha: Float = 0.25f,
    val blurRadius: Dp = 25.dp,
    val offsetY: Dp = 4.dp
) {
    companion object {
        val Default = ShadowConfig()
    }
}

/** Glassmorphism configuration */
data class GlassConfig(
    val blurIntensity: Dp = 20.dp,
    val tintAlpha: Float = 0.15f,
    val borderOpacity: Float = 0.3f,
    val noiseFactor: Float = 0.05f
) {
    companion object {
        val Default = GlassConfig()
    }
}

/** Card appearance configuration */
data class CardConfig(
    val cornerRadius: Dp = 25.dp,
    val backgroundOpacity: Float = 1f,
    val borderWidth: Dp = 1.dp,
    val borderOpacity: Float = 0.15f
) {
    companion object {
        val Default = CardConfig()
    }
}

/** Floating menus appearance configuration */
data class FloatMenuConfig(
    val contextualNavBarOffsetX: Dp = 16.dp, // end padding
    val contextualNavBarOffsetY: Dp = 90.dp, // bottom padding
    val cornerMenuOffsetX: Dp = 16.dp, // end padding
    val cornerMenuOffsetY: Dp = 80.dp // bottom padding
) {
    companion object {
        val Default = FloatMenuConfig()
    }
}

/** Aggregated user preferences state */
data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.Auto,
    val accentPreset: AccentPreset = AccentPreset.Cyan,
    val titleFont: FontOption = FontOption.Anton,
    val bodyFont: BodyFontOption = BodyFontOption.OneUiSans,
    val shadowConfig: ShadowConfig = ShadowConfig.Default,
    val glassConfig: GlassConfig = GlassConfig.Default,
    val cardConfig: CardConfig = CardConfig.Default,
    val floatMenuConfig: FloatMenuConfig = FloatMenuConfig.Default,
    val isOnboardingCompleted: Boolean = false
) {
    fun toConfigDump(): String = buildString {
        appendLine("═══ CeroFiao Config Dump ═══")
        appendLine()
        appendLine("Theme Mode: ${themeMode.name}")
        appendLine("Accent: ${accentPreset.id} (light=${accentPreset.lightColor}, dark=${accentPreset.darkColor})")
        appendLine("Title Font: ${titleFont.id}")
        appendLine("Body Font: ${bodyFont.id}")
        appendLine()
        appendLine("─── Shadows ───")
        appendLine("Enabled: ${shadowConfig.enabled}")
        appendLine("Alpha: ${shadowConfig.alpha}")
        appendLine("Blur Radius: ${shadowConfig.blurRadius}")
        appendLine("Offset Y: ${shadowConfig.offsetY}")
        appendLine()
        appendLine("─── Glass ───")
        appendLine("Blur Intensity: ${glassConfig.blurIntensity}")
        appendLine("Tint Alpha: ${glassConfig.tintAlpha}")
        appendLine("Border Opacity: ${glassConfig.borderOpacity}")
        appendLine("Noise Factor: ${glassConfig.noiseFactor}")
        appendLine()
        appendLine("─── Cards ───")
        appendLine("Corner Radius: ${cardConfig.cornerRadius}")
        appendLine("Background Opacity: ${cardConfig.backgroundOpacity}")
        appendLine("Border Width: ${cardConfig.borderWidth}")
        appendLine("Border Opacity: ${cardConfig.borderOpacity}")
        appendLine()
        appendLine("─── Floating Menus ───")
        appendLine("Contextual Nav X/Y: ${floatMenuConfig.contextualNavBarOffsetX} / ${floatMenuConfig.contextualNavBarOffsetY}")
        appendLine("Corner Menu X/Y: ${floatMenuConfig.cornerMenuOffsetX} / ${floatMenuConfig.cornerMenuOffsetY}")
        appendLine()
        appendLine("─── Onboarding ───")
        appendLine("Completed: $isOnboardingCompleted")
    }
}
