package com.schwarckdev.cerofiao.feature.settings

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.BrandGradient
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.model.ThemeMode
import com.schwarckdev.cerofiao.core.ui.GlassCard
import com.schwarckdev.cerofiao.core.ui.GlassCardPadding

@Composable
fun SettingsScreen(
    onNavigateToCategories: () -> Unit = {},
    onNavigateToExchangeRates: () -> Unit = {},
    onNavigateToBudgets: () -> Unit = {},
    onNavigateToDebts: () -> Unit = {},
    onNavigateToCsvExport: () -> Unit = {},
    onNavigateToRecurring: () -> Unit = {},
    onNavigateToAssociatedTitles: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val prefs by viewModel.preferences.collectAsStateWithLifecycle()
    val t = CeroFiaoTheme.tokens

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 60.dp, bottom = 100.dp),
    ) {
        // Title
        Text(
            text = "Ajustes",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = t.text,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Profile card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /* future: navigate to profile */ },
            padding = GlassCardPadding.Medium,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Avatar with brand gradient
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(BrandGradient),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "CF",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "CeroFiao",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = t.text,
                    )
                    Text(
                        text = "usuario@cerofiao.app",
                        fontSize = 12.sp,
                        color = t.textMuted,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Plan Pro badge
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(t.success),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Plan Pro",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = t.success,
                        )
                    }
                }

                Icon(
                    imageVector = CeroFiaoIcons.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = t.textFaint,
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // PREFERENCIAS section
        SectionLabel("PREFERENCIAS")
        Spacer(modifier = Modifier.height(8.dp))
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            padding = GlassCardPadding.None,
        ) {
            Column {
                SettingRow(
                    icon = CeroFiaoIcons.DarkMode,
                    iconColor = Color(0xFF8A2BE2),
                    label = "Modo Oscuro",
                    description = "OLED optimizado",
                    trailing = {
                        IOSToggle(
                            checked = prefs.themeMode == ThemeMode.DARK,
                            onCheckedChange = { checked ->
                                viewModel.setThemeMode(
                                    if (checked) ThemeMode.DARK else ThemeMode.LIGHT,
                                )
                            },
                        )
                    },
                )
                SettingDivider()
                SettingRow(
                    icon = CeroFiaoIcons.Notifications,
                    iconColor = Color(0xFF00BCD4),
                    label = "Notificaciones",
                    description = "Alertas de gastos y recordatorios",
                    trailing = {
                        IOSToggle(
                            checked = true,
                            onCheckedChange = { /* future */ },
                        )
                    },
                )
                SettingDivider()
                SettingRow(
                    icon = CeroFiaoIcons.Haptic,
                    iconColor = Color(0xFFFF9800),
                    label = "Vibraci\u00f3n h\u00e1ptica",
                    description = "Feedback t\u00e1ctil en acciones",
                    trailing = {
                        IOSToggle(
                            checked = true,
                            onCheckedChange = { /* future */ },
                        )
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // FINANZAS section
        SectionLabel("FINANZAS")
        Spacer(modifier = Modifier.height(8.dp))
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            padding = GlassCardPadding.None,
        ) {
            Column {
                SettingRow(
                    icon = CeroFiaoIcons.Globe,
                    iconColor = Color(0xFF4CAF50),
                    label = "Moneda Base",
                    description = "Para c\u00e1lculos de patrimonio",
                    onClick = onNavigateToExchangeRates,
                    trailing = {
                        CurrencyPill(code = prefs.displayCurrencyCode)
                    },
                )
                SettingDivider()
                SettingRow(
                    icon = CeroFiaoIcons.CurrencyIcon,
                    iconColor = Color(0xFFFFD700),
                    label = "Auto-conversi\u00f3n",
                    description = "Convertir VES a USD autom\u00e1ticamente",
                    trailing = {
                        IOSToggle(
                            checked = false,
                            onCheckedChange = { /* future */ },
                        )
                    },
                )
                SettingDivider()
                SettingRow(
                    icon = CeroFiaoIcons.Categories,
                    iconColor = Color(0xFF8A2BE2),
                    label = "Categor\u00edas",
                    description = "Personalizar \u00edconos y colores",
                    onClick = onNavigateToCategories,
                    trailing = {
                        Icon(
                            imageVector = CeroFiaoIcons.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = CeroFiaoTheme.tokens.textFaint,
                        )
                    },
                )
                SettingDivider()
                SettingRow(
                    icon = CeroFiaoIcons.ExportData,
                    iconColor = CeroFiaoTheme.tokens.textSecondary,
                    label = "Exportar datos",
                    description = "CSV, Excel o PDF",
                    onClick = onNavigateToCsvExport,
                    trailing = {
                        Icon(
                            imageVector = CeroFiaoIcons.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = CeroFiaoTheme.tokens.textFaint,
                        )
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // SEGURIDAD section
        SectionLabel("SEGURIDAD")
        Spacer(modifier = Modifier.height(8.dp))
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            padding = GlassCardPadding.None,
        ) {
            Column {
                SettingRow(
                    icon = CeroFiaoIcons.Biometric,
                    iconColor = Color(0xFF4CAF50),
                    label = "Biometr\u00eda",
                    description = "Face ID / Huella digital",
                    trailing = {
                        IOSToggle(
                            checked = false,
                            onCheckedChange = { /* future */ },
                        )
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // SMART FEATURES section
        SectionLabel("SMART FEATURES")
        Spacer(modifier = Modifier.height(8.dp))
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            padding = GlassCardPadding.None,
        ) {
            Column {
                SettingRow(
                    icon = CeroFiaoIcons.AIChat,
                    iconColor = Color(0xFF8A2BE2),
                    label = "Asistente IA",
                    description = "Chat inteligente y OCR",
                    onClick = { /* future */ },
                    trailing = {
                        Icon(
                            imageVector = CeroFiaoIcons.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = CeroFiaoTheme.tokens.textFaint,
                        )
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // SUPPORT section
        SectionLabel("SUPPORT")
        Spacer(modifier = Modifier.height(8.dp))
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            padding = GlassCardPadding.None,
        ) {
            Column {
                SettingRow(
                    icon = CeroFiaoIcons.Help,
                    iconColor = CeroFiaoTheme.tokens.textFaint,
                    label = "Ayuda y soporte",
                    onClick = { /* future */ },
                    trailing = {
                        Icon(
                            imageVector = CeroFiaoIcons.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = CeroFiaoTheme.tokens.textFaint,
                        )
                    },
                )
                SettingDivider()
                SettingRow(
                    icon = CeroFiaoIcons.Logout,
                    iconColor = CeroFiaoTheme.tokens.danger,
                    label = "Cerrar sesi\u00f3n",
                    labelColor = CeroFiaoTheme.tokens.danger,
                    onClick = { /* future */ },
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Version footer
        Text(
            text = "CeroFiao v2.0.0 \u00b7 Build 2026.03",
            fontSize = 11.sp,
            color = t.textGhost,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ─── Section Label ──────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(title: String) {
    val t = CeroFiaoTheme.tokens
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = t.textMuted,
        letterSpacing = 0.04.sp,
        modifier = Modifier.padding(start = 4.dp),
    )
}

// ─── Setting Row ────────────────────────────────────────────────────────────────

@Composable
private fun SettingRow(
    icon: ImageVector,
    iconColor: Color,
    label: String,
    description: String? = null,
    labelColor: Color = CeroFiaoTheme.tokens.text,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val t = CeroFiaoTheme.tokens

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                },
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Icon container
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconColor.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = iconColor,
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Label and description
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = labelColor,
            )
            if (description != null) {
                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = t.textMuted,
                )
            }
        }

        // Trailing content
        if (trailing != null) {
            Spacer(modifier = Modifier.width(8.dp))
            trailing()
        }
    }
}

// ─── Setting Divider ────────────────────────────────────────────────────────────

@Composable
private fun SettingDivider() {
    val t = CeroFiaoTheme.tokens
    HorizontalDivider(
        modifier = Modifier.padding(start = 66.dp),
        thickness = 0.5.dp,
        color = t.divider,
    )
}

// ─── Currency Pill ──────────────────────────────────────────────────────────────

@Composable
private fun CurrencyPill(code: String) {
    val t = CeroFiaoTheme.tokens
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = t.pillBg,
    ) {
        Text(
            text = code,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = t.textSecondary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        )
    }
}

// ─── iOS Toggle ─────────────────────────────────────────────────────────────────

@Composable
private fun IOSToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val trackWidth: Dp = 51.dp
    val trackHeight: Dp = 31.dp
    val thumbSize: Dp = 27.dp
    val thumbPadding: Dp = 2.dp

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize - thumbPadding else thumbPadding,
        animationSpec = tween(durationMillis = 200),
        label = "thumb_offset",
    )

    val t = CeroFiaoTheme.tokens
    val trackColor = if (checked) t.success else t.handleBg

    Box(
        modifier = modifier
            .size(width = trackWidth, height = trackHeight)
            .clip(CircleShape)
            .background(trackColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onCheckedChange(!checked) },
            ),
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .padding(vertical = thumbPadding)
                .size(thumbSize)
                .clip(CircleShape)
                .background(Color.White),
        )
    }
}
