package com.schwarckdev.cerofiao.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons

// Legacy Composition Locals mapping
val LocalBlurEnabled = staticCompositionLocalOf { false }

data class GlassConfig(val blurIntensity: androidx.compose.ui.unit.Dp, val tintAlpha: Float, val borderOpacity: Float)
val LocalGlassConfig = staticCompositionLocalOf { GlassConfig(24.dp, 0.85f, 0.5f) }

object CeroFiaoDesign {
    object componentSize {
        val cornerMenuWidth = 220.dp
        val cornerMenuItemPaddingVertical = 4.dp
        val cornerMenuItemPaddingHorizontal = 9.dp
        val cornerMenuPaddingVertical = 8.dp
        val cornerMenuPaddingHorizontal = 8.dp
        val navItemWidth = 64.dp
        val navItemPaddingVertical = 4.dp
        val navItemPaddingHorizontal = 4.dp
        val topBarButtonSize = 40.dp
    }
    
    object spacing {
        val xxxs = 2.dp
        val xxs = 4.dp
        val xs = 6.dp
        val sm = 8.dp
        val md = 16.dp
        val lg = 24.dp
    }
    
    object radius {
        val cornerMenu = 24.dp
        val sm = 8.dp
        val md = 16.dp
        val lg = 24.dp
        val full = 999.dp
        val pill = 50.dp
        val circle = 50.dp
    }
    
    object iconSize {
        val sm = 16.dp
        val md = 24.dp
        val lg = 32.dp
    }
    
    object components {
        val buttonHeight = 48.dp
    }
}

object CeroFiaoTextStyles {
    val NavLabel = TextStyle(fontWeight = FontWeight.Medium)
}

// Map old color token names to the new CeroFiaoColorTokens structure seamlessly
val CeroFiaoColorTokens.GlassBackground get() = this.surface
val CeroFiaoColorTokens.TextPrimary get() = this.text
val CeroFiaoColorTokens.TextSecondary get() = this.textSecondary
val CeroFiaoColorTokens.CardBorder get() = this.surfaceBorder
val CeroFiaoColorTokens.fondoMenus get() = this.bgModal
val CeroFiaoColorTokens.ActiveItemBackground get() = this.success.copy(alpha = 0.2f)
val CeroFiaoColorTokens.InactiveColor get() = this.textMuted
val CeroFiaoColorTokens.AccentBlue get() = Color(0xFF007AFF)
val CeroFiaoColorTokens.ShadowColor get() = Color.Black
