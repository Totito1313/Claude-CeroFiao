package com.schwarckdev.cerofiao.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.R

// ═══════════════════════════════════════════════════════════════
// CeroFiao Design System - Typography
// Adapted from LionFitness
// ═══════════════════════════════════════════════════════════════

val OneUiSansFontFamily = FontFamily(
    Font(R.font.variable_one_ui_sans, FontWeight.Normal),
    Font(R.font.variable_one_ui_sans, FontWeight.Medium),
    Font(R.font.variable_one_ui_sans, FontWeight.Bold)
)

val LionFontFamily = OneUiSansFontFamily

val AntonFontFamily = FontFamily(
    Font(R.font.anton_regular, FontWeight.Normal)
)

val BebasNeueFontFamily = FontFamily(
    Font(R.font.bebas_neue_regular, FontWeight.Normal)
)

val BarlowFontFamily = FontFamily(
    Font(R.font.barlow_bold, FontWeight.Bold)
)

val QuicksandFontFamily = FontFamily(
    Font(R.font.quicksand_variable, FontWeight.Normal),
    Font(R.font.quicksand_variable, FontWeight.Medium),
    Font(R.font.quicksand_variable, FontWeight.Bold)
)

val AsapFontFamily = FontFamily(
    Font(R.font.asap_thin, FontWeight.Normal)
)

val InterFontFamily = FontFamily(
    Font(R.font.inter_variable, FontWeight.Normal),
    Font(R.font.inter_variable, FontWeight.Medium),
    Font(R.font.inter_variable, FontWeight.Bold)
)

fun getCeroFiaoTypography(
    titleFontFamily: FontFamily,
    bodyFontFamily: FontFamily
): Typography {
    return Typography(
        displayLarge = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = if (titleFontFamily == AntonFontFamily) 1.sp else 0.sp
        ),
        displayMedium = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = if (titleFontFamily == AntonFontFamily) 1.sp else 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = if (titleFontFamily == AntonFontFamily) 1.sp else 0.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = if (titleFontFamily == AntonFontFamily) 1.sp else 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = if (titleFontFamily == AntonFontFamily) 1.sp else 0.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = if (titleFontFamily == AntonFontFamily) 1.sp else 0.sp
        ),
        titleLarge = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        titleSmall = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),
        labelLarge = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}

// Current backwards-compatible mapping for CeroFiao
val CeroFiaoTypography = getCeroFiaoTypography(AntonFontFamily, OneUiSansFontFamily)

// Custom text styles for specific use cases
object CeroFiaoTextStyles {
    val TopBarTitle = TextStyle(
        fontFamily = AntonFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    
    val ChipText = TextStyle(
        fontFamily = LionFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp
    )
    
    val StatValue = TextStyle(
        fontFamily = AntonFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = 1.sp
    )
    
    fun getTopBarTitle(fontFamily: FontFamily) = TopBarTitle.copy(
        fontFamily = fontFamily,
        letterSpacing = if (fontFamily == AntonFontFamily) 0.5.sp else 0.sp
    )
    
    val NavLabel = TextStyle(
        fontFamily = LionFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.sp
    )
    
    fun getStatValue(fontFamily: FontFamily) = StatValue.copy(
        fontFamily = fontFamily,
        letterSpacing = if (fontFamily == AntonFontFamily) 1.sp else 0.sp
    )
}
