package com.schwarckdev.cerofiao.core.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat

// Custom composition locals for design tokens
val LocalCeroFiaoColors = staticCompositionLocalOf { LightCeroFiaoColors }
val LocalCeroFiaoEffects = staticCompositionLocalOf { LightCeroFiaoEffects }
val LocalSpacing = staticCompositionLocalOf { Spacing }
val LocalRadius = staticCompositionLocalOf { Radius }
val LocalElevation = staticCompositionLocalOf { Elevation }
val LocalIconSize = staticCompositionLocalOf { IconSize }
val LocalComponentSize = staticCompositionLocalOf { ComponentSize }
val LocalTitleFontFamily = staticCompositionLocalOf { AntonFontFamily }
val LocalBodyFontFamily = staticCompositionLocalOf { OneUiSansFontFamily }
val LocalBlurEnabled = staticCompositionLocalOf { false }
val LocalShadowConfig = staticCompositionLocalOf { ShadowConfig.Default }
val LocalGlassConfig = staticCompositionLocalOf { GlassConfig.Default }
val LocalCardConfig = staticCompositionLocalOf { CardConfig.Default }

@Composable
fun CeroFiaoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    titleFontFamily: FontFamily = AntonFontFamily,
    bodyFontFamily: FontFamily = OneUiSansFontFamily,
    accentColor: Color? = null,
    shadowConfig: ShadowConfig = ShadowConfig.Default,
    glassConfig: GlassConfig = GlassConfig.Default,
    cardConfig: CardConfig = CardConfig.Default,
    content: @Composable () -> Unit
) {
    val baseCeroFiaoColors = if (darkTheme) DarkCeroFiaoColors else LightCeroFiaoColors

    // Apply accent color override if provided
    val ceroFiaoColors = if (accentColor != null) {
        baseCeroFiaoColors.copy(
            Primary = accentColor,
            GradientAccent = accentColor,
            AccentUser = accentColor
        )
    } else {
        baseCeroFiaoColors
    }

    val ceroFiaoEffects = if (darkTheme) DarkCeroFiaoEffects else LightCeroFiaoEffects
    
    // Disable blur on older devices for performance/compatibility
    val isBlurEnabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    
    val typography = getCeroFiaoTypography(titleFontFamily, bodyFontFamily)

    // Update Material 3 generic scheme to match our custom tokens roughly
    val materialColorScheme = if (darkTheme) {
        darkColorScheme(
            primary = ceroFiaoColors.Primary,
            onPrimary = ceroFiaoColors.OnPrimary,
            background = ceroFiaoColors.Background,
            onBackground = ceroFiaoColors.TextPrimary,
            surface = ceroFiaoColors.Surface,
            onSurface = ceroFiaoColors.TextPrimary,
            error = ceroFiaoColors.Error
        )
    } else {
        lightColorScheme(
            primary = ceroFiaoColors.Primary,
            onPrimary = ceroFiaoColors.OnPrimary,
            background = ceroFiaoColors.Background,
            onBackground = ceroFiaoColors.TextPrimary,
            surface = ceroFiaoColors.Surface,
            onSurface = ceroFiaoColors.TextPrimary,
            error = ceroFiaoColors.Error
        )
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = ceroFiaoColors.Background.toArgb()
            window.navigationBarColor = ceroFiaoColors.NavBackground.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }
    
    CompositionLocalProvider(
        LocalCeroFiaoColors provides ceroFiaoColors,
        LocalCeroFiaoEffects provides ceroFiaoEffects,
        LocalSpacing provides Spacing,
        LocalRadius provides Radius,
        LocalElevation provides Elevation,
        LocalIconSize provides IconSize,
        LocalComponentSize provides ComponentSize,
        LocalTitleFontFamily provides titleFontFamily,
        LocalBodyFontFamily provides bodyFontFamily,
        LocalBlurEnabled provides isBlurEnabled,
        LocalShadowConfig provides shadowConfig,
        LocalGlassConfig provides glassConfig,
        LocalCardConfig provides cardConfig
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = typography,
            content = content
        )
    }
}

// Extension properties for easy access to custom tokens
object CeroFiaoDesign {
    val colors: CeroFiaoColors
        @Composable
        get() = LocalCeroFiaoColors.current

    val effects: CeroFiaoEffects
        @Composable
        get() = LocalCeroFiaoEffects.current

    val spacing: Spacing
        @Composable
        get() = LocalSpacing.current
    
    val radius: Radius
        @Composable
        get() = LocalRadius.current
    
    val elevation: Elevation
        @Composable
        get() = LocalElevation.current
    
    val iconSize: IconSize
        @Composable
        get() = LocalIconSize.current
    
    val componentSize: ComponentSize
        @Composable
        get() = LocalComponentSize.current

    val blurEnabled: Boolean
        @Composable
        get() = LocalBlurEnabled.current

    val shadowConfig: ShadowConfig
        @Composable
        get() = LocalShadowConfig.current
}
