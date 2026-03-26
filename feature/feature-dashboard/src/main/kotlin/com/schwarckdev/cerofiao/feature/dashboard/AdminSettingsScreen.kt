package com.schwarckdev.cerofiao.feature.dashboard

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import android.widget.Toast
import com.schwarckdev.cerofiao.core.designsystem.components.CeroFiaoGlassCard
import com.schwarckdev.cerofiao.core.designsystem.components.bounceClick
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.AccentPreset
import com.schwarckdev.cerofiao.core.designsystem.theme.BodyFontOption
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoColors
import com.schwarckdev.cerofiao.core.designsystem.theme.FontOption
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCeroFiaoColors
import com.schwarckdev.cerofiao.core.designsystem.theme.ThemeMode

@Composable
fun AdminSettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AdminSettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val colors = LocalCeroFiaoColors.current
    var showIconViewer by remember { mutableStateOf(false) }

    ConfigureTopBar(
        variant = TopBarVariant.Standard,
        title = "Admin Settings",
        onBackClick = onNavigateBack
    )

    Box(modifier = Modifier.fillMaxSize().background(colors.Background)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ═══ Header ═══
            item {
                Text(
                    text = "Personaliza tu experiencia",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.TextSecondary
                )
            }

            // ═══ 1. Theme Mode ═══
            item {
                SectionTitle(title = "Apariencia")
                Spacer(modifier = Modifier.height(8.dp))
                ThemeModeSelector(
                    currentMode = state.themeMode,
                    onModeSelected = { viewModel.setThemeMode(it) }
                )
            }

            // ═══ 2. Accent Color ═══
            item {
                SectionTitle(title = "Color de acento")
                Spacer(modifier = Modifier.height(8.dp))
                AccentColorPicker(
                    currentPreset = state.accentPreset,
                    onPresetSelected = { viewModel.setAccentPreset(it) }
                )
            }

            // ═══ 3. Title Font ═══
            item {
                SectionTitle(title = "Tipografía de títulos")
                Spacer(modifier = Modifier.height(8.dp))
                FontSelector(
                    options = FontOption.entries,
                    selectedId = state.titleFont.id,
                    onSelected = { viewModel.setTitleFont(it) },
                    sampleText = "CeroFiao"
                )
            }

            // ═══ 4. Body Font ═══
            item {
                SectionTitle(title = "Tipografía del cuerpo")
                Spacer(modifier = Modifier.height(8.dp))
                BodyFontSelector(
                    options = BodyFontOption.entries,
                    selectedId = state.bodyFont.id,
                    onSelected = { viewModel.setBodyFont(it) }
                )
            }

            // ═══ 5. Shadows ═══
            item {
                SectionTitle(title = "Sombras")
                Spacer(modifier = Modifier.height(8.dp))
                ShadowControls(
                    enabled = state.shadowConfig.enabled,
                    alpha = state.shadowConfig.alpha,
                    blur = state.shadowConfig.blurRadius.value,
                    offsetY = state.shadowConfig.offsetY.value,
                    onEnabledChanged = { viewModel.setShadowEnabled(it) },
                    onAlphaChanged = { viewModel.setShadowAlpha(it) },
                    onBlurChanged = { viewModel.setShadowBlur(it) },
                    onOffsetYChanged = { viewModel.setShadowOffsetY(it) }
                )
            }

            // ═══ 6. Glassmorphism ═══
            item {
                SectionTitle(title = "Glassmorfismo")
                Spacer(modifier = Modifier.height(8.dp))
                GlassControls(
                    blur = state.glassConfig.blurIntensity.value,
                    tintAlpha = state.glassConfig.tintAlpha,
                    borderOpacity = state.glassConfig.borderOpacity,
                    noise = state.glassConfig.noiseFactor,
                    onBlurChanged = { viewModel.setGlassBlur(it) },
                    onTintAlphaChanged = { viewModel.setGlassTintAlpha(it) },
                    onBorderOpacityChanged = { viewModel.setGlassBorderOpacity(it) },
                    onNoiseChanged = { viewModel.setGlassNoise(it) }
                )
            }

            // ═══ 7. Cards ═══
            item {
                SectionTitle(title = "Tarjetas")
                Spacer(modifier = Modifier.height(8.dp))
                CardControls(
                    cornerRadius = state.cardConfig.cornerRadius.value,
                    bgOpacity = state.cardConfig.backgroundOpacity,
                    borderWidth = state.cardConfig.borderWidth.value,
                    borderOpacity = state.cardConfig.borderOpacity,
                    onCornerRadiusChanged = { viewModel.setCardCornerRadius(it) },
                    onBgOpacityChanged = { viewModel.setCardBgOpacity(it) },
                    onBorderWidthChanged = { viewModel.setCardBorderWidth(it) },
                    onBorderOpacityChanged = { viewModel.setCardBorderOpacity(it) }
                )
            }

            // ═══ 8. Float Menus ═══
            item {
                SectionTitle(title = "Menús Flotantes")
                Spacer(modifier = Modifier.height(8.dp))
                FloatMenuControls(
                    contextualX = state.floatMenuConfig.contextualNavBarOffsetX.value,
                    contextualY = state.floatMenuConfig.contextualNavBarOffsetY.value,
                    cornerX = state.floatMenuConfig.cornerMenuOffsetX.value,
                    cornerY = state.floatMenuConfig.cornerMenuOffsetY.value,
                    onContextualXChanged = { viewModel.setContextualNavBarOffsetX(it) },
                    onContextualYChanged = { viewModel.setContextualNavBarOffsetY(it) },
                    onCornerXChanged = { viewModel.setCornerMenuOffsetX(it) },
                    onCornerYChanged = { viewModel.setCornerMenuOffsetY(it) }
                )
            }

            // ═══ 9. Icon Viewer ═══
            item {
                SectionTitle(title = "Iconos")
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(colors.Primary.copy(alpha = 0.1f))
                        .bounceClick(onClick = { showIconViewer = true })
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Explorador de Iconos",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.Primary
                    )
                }
            }

            // ═══ 10. Reset ═══
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(colors.Error.copy(alpha = 0.1f))
                        .bounceClick(onClick = { viewModel.resetToDefaults() })
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Restablecer valores por defecto",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.Error
                    )
                }
            }

            // ═══ Config Dump ═══
            item {
                SectionTitle(title = "Configuración actual")
                Spacer(modifier = Modifier.height(8.dp))
                CeroFiaoGlassCard(modifier = Modifier.fillMaxWidth()) {
                    SelectionContainer {
                        Text(
                            text = state.toConfigDump(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal,
                                lineHeight = 14.sp
                            ),
                            color = colors.TextSecondary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            // ═══ Version ═══
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CeroFiao v1.0.0",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = colors.TextSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Hecho con ♡ en Venezuela",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = colors.TextSecondary
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }

        if (showIconViewer) {
            IconViewerOverlay(
                onDismiss = { showIconViewer = false },
                colors = colors
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Section Title
// ═══════════════════════════════════════════════════════════════

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = LocalCeroFiaoColors.current.TextPrimary
    )
}

// ═══════════════════════════════════════════════════════════════
// 1. Theme Mode Selector
// ═══════════════════════════════════════════════════════════════

@Composable
private fun ThemeModeSelector(
    currentMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit
) {
    val colors = LocalCeroFiaoColors.current

    CeroFiaoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ThemeMode.entries.forEach { mode ->
                val isSelected = mode == currentMode
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) colors.Primary.copy(alpha = 0.15f) else Color.Transparent,
                    animationSpec = spring(stiffness = Spring.StiffnessMedium),
                    label = "modeBg"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) colors.Primary else colors.TextSecondary,
                    animationSpec = spring(stiffness = Spring.StiffnessMedium),
                    label = "modeText"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(bgColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onModeSelected(mode) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        ThemeModeIcon(mode = mode, isSelected = isSelected, tint = textColor)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = mode.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeModeIcon(mode: ThemeMode, isSelected: Boolean, tint: Color) {
    val colors = LocalCeroFiaoColors.current
    val moonCutoutColor = if (isSelected) colors.Primary.copy(alpha = 0.15f) else colors.CardBackground
    Canvas(modifier = Modifier.size(20.dp)) {
        when (mode) {
            ThemeMode.Light -> {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 4
                drawCircle(color = tint, radius = radius, center = center)
                for (i in 0 until 8) {
                    val angle = Math.toRadians(i * 45.0)
                    val startR = radius * 1.5f
                    val endR = radius * 2.2f
                    drawLine(
                        color = tint,
                        start = Offset(
                            (center.x + startR * kotlin.math.cos(angle)).toFloat(),
                            (center.y + startR * kotlin.math.sin(angle)).toFloat()
                        ),
                        end = Offset(
                            (center.x + endR * kotlin.math.cos(angle)).toFloat(),
                            (center.y + endR * kotlin.math.sin(angle)).toFloat()
                        ),
                        strokeWidth = 1.5f
                    )
                }
            }
            ThemeMode.Dark -> {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 3
                drawCircle(color = tint, radius = radius, center = center)
                drawCircle(
                    color = moonCutoutColor,
                    radius = radius * 0.8f,
                    center = Offset(center.x + radius * 0.4f, center.y - radius * 0.3f)
                )
            }
            ThemeMode.Auto -> {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 3
                drawCircle(color = tint, radius = radius, center = center, style = Stroke(width = 2f))
                drawArc(
                    color = tint,
                    startAngle = -90f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// 2. Accent Color Picker
// ═══════════════════════════════════════════════════════════════

@Composable
private fun AccentColorPicker(
    currentPreset: AccentPreset,
    onPresetSelected: (AccentPreset) -> Unit
) {
    val colors = LocalCeroFiaoColors.current

    CeroFiaoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AccentPreset.entries.forEach { preset ->
                    val isSelected = preset == currentPreset
                    val displayColor = preset.darkColor

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .then(
                                if (isSelected) Modifier.border(2.5.dp, colors.TextPrimary, CircleShape) else Modifier
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onPresetSelected(preset) },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 32.dp else 40.dp)
                                .clip(CircleShape)
                                .background(displayColor)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = currentPreset.label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = colors.TextSecondary
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// 3. Title Font Selector
// ═══════════════════════════════════════════════════════════════

@Composable
private fun FontSelector(
    options: List<FontOption>,
    selectedId: String,
    onSelected: (FontOption) -> Unit,
    sampleText: String
) {
    val colors = LocalCeroFiaoColors.current

    CeroFiaoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            options.forEachIndexed { index, option ->
                val isSelected = option.id == selectedId
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) colors.Primary.copy(alpha = 0.1f) else Color.Transparent,
                    label = "fontBg"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onSelected(option) }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sampleText,
                        fontFamily = option.toFontFamily(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (isSelected) colors.Primary else colors.TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = option.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = colors.TextSecondary
                    )
                    if (isSelected) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(colors.Primary))
                    }
                }

                if (index < options.size - 1) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                            .height(0.5.dp).background(colors.CardBorder)
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// 4. Body Font Selector
// ═══════════════════════════════════════════════════════════════

@Composable
private fun BodyFontSelector(
    options: List<BodyFontOption>,
    selectedId: String,
    onSelected: (BodyFontOption) -> Unit
) {
    val colors = LocalCeroFiaoColors.current

    CeroFiaoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            options.forEachIndexed { index, option ->
                val isSelected = option.id == selectedId
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) colors.Primary.copy(alpha = 0.1f) else Color.Transparent,
                    label = "bodyFontBg"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onSelected(option) }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "El rápido zorro marrón",
                        fontFamily = option.toFontFamily(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (isSelected) colors.Primary else colors.TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(text = option.label, style = MaterialTheme.typography.labelSmall, color = colors.TextSecondary)
                    if (isSelected) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(colors.Primary))
                    }
                }

                if (index < options.size - 1) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                            .height(0.5.dp).background(colors.CardBorder)
                    )
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// 5. Shadow Controls
// ═══════════════════════════════════════════════════════════════

@Composable
private fun ShadowControls(
    enabled: Boolean, alpha: Float, blur: Float, offsetY: Float,
    onEnabledChanged: (Boolean) -> Unit, onAlphaChanged: (Float) -> Unit,
    onBlurChanged: (Float) -> Unit, onOffsetYChanged: (Float) -> Unit
) {
    val colors = LocalCeroFiaoColors.current

    CeroFiaoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Sombras activas", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = colors.TextPrimary)
                    Text(if (enabled) "Habilitadas" else "Deshabilitadas", style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp), color = colors.TextSecondary)
                }
                Switch(
                    checked = enabled, onCheckedChange = onEnabledChanged,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = colors.Primary, checkedTrackColor = colors.Primary.copy(alpha = 0.3f),
                        uncheckedThumbColor = colors.TextSecondary, uncheckedTrackColor = colors.SurfaceVariant
                    )
                )
            }

            if (enabled) {
                Spacer(modifier = Modifier.height(16.dp))
                ShadowPreviewBox(alpha = alpha, blur = blur, offsetY = offsetY)
                Spacer(modifier = Modifier.height(16.dp))
                SliderControl("Opacidad", alpha, 0.05f..0.8f, "${(alpha * 100).toInt()}%", onAlphaChanged)
                Spacer(modifier = Modifier.height(12.dp))
                SliderControl("Desenfoque", blur, 5f..50f, "${blur.toInt()}dp", onBlurChanged)
                Spacer(modifier = Modifier.height(12.dp))
                SliderControl("Desplazamiento Y", offsetY, 0f..20f, "${offsetY.toInt()}dp", onOffsetYChanged)
            }
        }
    }
}

@Composable
private fun ShadowPreviewBox(alpha: Float, blur: Float, offsetY: Float) {
    val colors = LocalCeroFiaoColors.current
    Box(modifier = Modifier.fillMaxWidth().height(60.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(width = 120.dp, height = 50.dp)) {
            drawRoundRect(color = colors.ShadowColor.copy(alpha = alpha), topLeft = Offset(0f, offsetY.dp.toPx()), size = Size(size.width, size.height - offsetY.dp.toPx()), cornerRadius = CornerRadius(12.dp.toPx()))
            drawRoundRect(color = colors.CardBackground, topLeft = Offset.Zero, size = Size(size.width, size.height - offsetY.dp.toPx()), cornerRadius = CornerRadius(12.dp.toPx()))
            drawRoundRect(color = colors.CardBorder.copy(alpha = 0.2f), topLeft = Offset.Zero, size = Size(size.width, size.height - offsetY.dp.toPx()), cornerRadius = CornerRadius(12.dp.toPx()), style = Stroke(width = 1f))
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// 6. Glass Controls
// ═══════════════════════════════════════════════════════════════

@Composable
private fun GlassControls(
    blur: Float, tintAlpha: Float, borderOpacity: Float, noise: Float,
    onBlurChanged: (Float) -> Unit, onTintAlphaChanged: (Float) -> Unit,
    onBorderOpacityChanged: (Float) -> Unit, onNoiseChanged: (Float) -> Unit
) {
    CeroFiaoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            SliderControl("Intensidad de desenfoque", blur, 5f..60f, "${blur.toInt()}dp", onBlurChanged)
            Spacer(modifier = Modifier.height(12.dp))
            SliderControl("Opacidad del tinte", tintAlpha, 0.0f..0.5f, "${(tintAlpha * 100).toInt()}%", onTintAlphaChanged)
            Spacer(modifier = Modifier.height(12.dp))
            SliderControl("Opacidad del borde", borderOpacity, 0.0f..1.0f, "${(borderOpacity * 100).toInt()}%", onBorderOpacityChanged)
            Spacer(modifier = Modifier.height(12.dp))
            SliderControl("Factor de ruido", noise, 0.0f..0.2f, "${(noise * 100).toInt()}%", onNoiseChanged)
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// 7. Card Controls
// ═══════════════════════════════════════════════════════════════

@Composable
private fun CardControls(
    cornerRadius: Float, bgOpacity: Float, borderWidth: Float, borderOpacity: Float,
    onCornerRadiusChanged: (Float) -> Unit, onBgOpacityChanged: (Float) -> Unit,
    onBorderWidthChanged: (Float) -> Unit, onBorderOpacityChanged: (Float) -> Unit
) {
    CeroFiaoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            SliderControl("Radio de esquinas", cornerRadius, 0f..40f, "${cornerRadius.toInt()}dp", onCornerRadiusChanged)
            Spacer(modifier = Modifier.height(12.dp))
            SliderControl("Opacidad del fondo", bgOpacity, 0.0f..1.0f, "${(bgOpacity * 100).toInt()}%", onBgOpacityChanged)
            Spacer(modifier = Modifier.height(12.dp))
            SliderControl("Grosor del borde", borderWidth, 0f..4f, "${String.format(java.util.Locale.US, "%.1f", borderWidth)}dp", onBorderWidthChanged)
            Spacer(modifier = Modifier.height(12.dp))
            SliderControl("Opacidad del borde", borderOpacity, 0.0f..1.0f, "${(borderOpacity * 100).toInt()}%", onBorderOpacityChanged)
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// 8. Float Menu Controls
// ═══════════════════════════════════════════════════════════════

@Composable
private fun FloatMenuControls(
    contextualX: Float, contextualY: Float, cornerX: Float, cornerY: Float,
    onContextualXChanged: (Float) -> Unit, onContextualYChanged: (Float) -> Unit,
    onCornerXChanged: (Float) -> Unit, onCornerYChanged: (Float) -> Unit
) {
    val colors = LocalCeroFiaoColors.current
    CeroFiaoGlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Barra Contextual (Stats/Deudas)", style = MaterialTheme.typography.labelSmall, color = colors.TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            SliderControl("Margen Derecho (X)", contextualX, 0f..100f, "${contextualX.toInt()}dp", onContextualXChanged)
            Spacer(modifier = Modifier.height(12.dp))
            SliderControl("Margen Inferior (Y)", contextualY, 0f..200f, "${contextualY.toInt()}dp", onContextualYChanged)
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(colors.CardBorder))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Menú Esquina (Cuentas/Ajustes)", style = MaterialTheme.typography.labelSmall, color = colors.TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            SliderControl("Margen Derecho (X)", cornerX, 0f..100f, "${cornerX.toInt()}dp", onCornerXChanged)
            Spacer(modifier = Modifier.height(12.dp))
            SliderControl("Margen Inferior (Y)", cornerY, 0f..200f, "${cornerY.toInt()}dp", onCornerYChanged)
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// Shared Slider Control
// ═══════════════════════════════════════════════════════════════

@Composable
private fun SliderControl(
    label: String, value: Float, valueRange: ClosedFloatingPointRange<Float>,
    displayValue: String, onValueChange: (Float) -> Unit
) {
    val colors = LocalCeroFiaoColors.current
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = colors.TextSecondary)
            Text(text = displayValue, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = colors.Primary)
        }
        Slider(
            value = value, onValueChange = onValueChange, valueRange = valueRange,
            colors = SliderDefaults.colors(thumbColor = colors.Primary, activeTrackColor = colors.Primary, inactiveTrackColor = colors.SurfaceVariant),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ═══════════════════════════════════════════════════════════════
// Icon Viewer Overlay
// ═══════════════════════════════════════════════════════════════

@Composable
private fun IconViewerOverlay(
    onDismiss: () -> Unit,
    colors: CeroFiaoColors
) {
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    @Suppress("DEPRECATION")
    val clipboardManager = LocalClipboardManager.current

    val allIcons = remember {
        CeroFiaoIcons::class.java.methods
            .filter { it.returnType == ImageVector::class.java }
            .map { it.name.removePrefix("get") to it.invoke(CeroFiaoIcons) as ImageVector }
    }

    val filteredIcons = remember(searchQuery) {
        if (searchQuery.isBlank()) allIcons
        else allIcons.filter { it.first.contains(searchQuery, ignoreCase = true) }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.Background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp)
            ) {
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar icono...", color = colors.TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.CardBackground,
                            unfocusedContainerColor = colors.CardBackground,
                            focusedBorderColor = colors.Primary,
                            unfocusedBorderColor = colors.CardBorder,
                            focusedTextColor = colors.TextPrimary,
                            unfocusedTextColor = colors.TextPrimary
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 80.dp),
                    contentPadding = PaddingValues(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredIcons, key = { it.first }) { (name, vector) ->
                        CeroFiaoGlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .bounceClick(onClick = {
                                    val codeSnippet = "CeroFiaoIcons.$name"
                                    clipboardManager.setText(AnnotatedString(codeSnippet))
                                    Toast.makeText(context, "Copiado: $name", Toast.LENGTH_SHORT).show()
                                })
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(imageVector = vector, contentDescription = name, modifier = Modifier.size(32.dp), tint = colors.TextPrimary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = name, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = colors.TextSecondary, textAlign = TextAlign.Center, maxLines = 2)
                            }
                        }
                    }
                }
            }

            ConfigureTopBar(
                variant = TopBarVariant.Standard,
                title = "Explorador de Iconos",
                onBackClick = onDismiss
            )
        }
    }
}
