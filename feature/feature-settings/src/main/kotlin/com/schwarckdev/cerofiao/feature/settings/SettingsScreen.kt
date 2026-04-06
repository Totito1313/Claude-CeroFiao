package com.schwarckdev.cerofiao.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.Coins
import com.composables.icons.lucide.FileSpreadsheet
import com.composables.icons.lucide.Grid2x2
import com.composables.icons.lucide.Landmark
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Moon
import com.composables.icons.lucide.Palette
import com.composables.icons.lucide.Repeat
import com.composables.icons.lucide.Sun
import com.composables.icons.lucide.Tag
import com.composables.icons.lucide.TrendingUp
import com.composables.icons.lucide.Wallet
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonGroupItem
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.ButtonSize
import com.schwarckdev.cerofiao.core.designsystem.components.buttons.CeroFiaoButtonGroup
import com.schwarckdev.cerofiao.core.designsystem.components.controls.CeroFiaoSwitch
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoSelect
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.ExchangeRateSource
import com.schwarckdev.cerofiao.core.model.ThemeMode

// ── Display currency options ────────────────────────────────────
private data class CurrencyOption(
    val code: String,
    val symbol: String,
    val name: String,
)

private val currencyOptions = listOf(
    CurrencyOption("USD", "$", "Dolar (USD)"),
    CurrencyOption("VES", "Bs", "Bolivares (VES)"),
    CurrencyOption("USDT", "\u20AE", "Tether (USDT)"),
    CurrencyOption("EUR", "\u20AC", "Euro (EUR)"),
    CurrencyOption("EURI", "\u20AC", "Euro Informal (EURI)"),
)

// ── Rate source options (only user-selectable ones) ─────────────
private data class RateSourceOption(
    val source: ExchangeRateSource,
    val label: String,
    val description: String,
)

private val rateSourceOptions = listOf(
    RateSourceOption(ExchangeRateSource.BCV, "BCV (Oficial)", "Tasa oficial del Banco Central"),
    RateSourceOption(ExchangeRateSource.USDT, "Paralelo (USDT)", "Tasa del mercado paralelo"),
)

@Composable
fun SettingsScreen(
    onNavigateToCategories: () -> Unit = {},
    onNavigateToExchangeRates: () -> Unit = {},
    onNavigateToBudgets: () -> Unit = {},
    onNavigateToDebts: () -> Unit = {},
    onNavigateToCsvExport: () -> Unit = {},
    onNavigateToRecurring: () -> Unit = {},
    onNavigateToAssociatedTitles: () -> Unit = {},
    onNavigateToGoals: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    ConfigureTopBar(variant = TopBarVariant.Standard, title = "Ajustes")

    val prefs by viewModel.preferences.collectAsStateWithLifecycle()
    val colors = CeroFiaoDesign.colors

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 110.dp, top = 70.dp),
    ) {
        // ════════════════════════════════════════════════════════════
        // MONEDA Y TASAS
        // ════════════════════════════════════════════════════════════
        SectionLabel("MONEDA Y TASAS")

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = colors.Foreground,
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Display currency
                CeroFiaoSelect(
                    selected = currencyOptions.find { it.code == prefs.displayCurrencyCode },
                    onSelectedChange = { viewModel.setDisplayCurrency(it.code) },
                    options = currencyOptions,
                    label = "Moneda de referencia",
                    displayText = { "${it.symbol}  ${it.name}" },
                    description = "Moneda principal para mostrar totales y balances en toda la app",
                )

                Spacer(Modifier.height(20.dp))

                // Preferred rate source
                CeroFiaoSelect(
                    selected = rateSourceOptions.find { it.source == prefs.preferredRateSource },
                    onSelectedChange = { viewModel.setPreferredRateSource(it.source) },
                    options = rateSourceOptions,
                    label = "Fuente de tasa preferida",
                    displayText = { it.label },
                    description = "Fuente de cambio usada por defecto para conversiones directas (USD\u2194VES, EUR\u2194VES)",
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // ════════════════════════════════════════════════════════════
        // APARIENCIA
        // ════════════════════════════════════════════════════════════
        SectionLabel("APARIENCIA")

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = colors.Foreground,
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Theme mode
                Text(
                    text = "Tema",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.TextSecondary,
                    modifier = Modifier.padding(bottom = 10.dp, start = 4.dp),
                )

                CeroFiaoButtonGroup(
                    items = listOf(
                        ButtonGroupItem(
                            key = "light",
                            text = "Claro",
                            icon = Lucide.Sun,
                            isActive = prefs.themeMode == ThemeMode.LIGHT,
                            onClick = { viewModel.setThemeMode(ThemeMode.LIGHT) },
                        ),
                        ButtonGroupItem(
                            key = "dark",
                            text = "Oscuro",
                            icon = Lucide.Moon,
                            isActive = prefs.themeMode == ThemeMode.DARK,
                            onClick = { viewModel.setThemeMode(ThemeMode.DARK) },
                        ),
                        ButtonGroupItem(
                            key = "system",
                            text = "Sistema",
                            isActive = prefs.themeMode == ThemeMode.SYSTEM,
                            onClick = { viewModel.setThemeMode(ThemeMode.SYSTEM) },
                        ),
                    ),
                    size = ButtonSize.Medium,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(20.dp))

                // Dynamic color
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Color dinamico",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.TextPrimary,
                        )
                        Text(
                            text = "Usa los colores de tu fondo de pantalla",
                            fontSize = 13.sp,
                            color = colors.TextSecondary,
                        )
                    }
                    CeroFiaoSwitch(
                        checked = prefs.useDynamicColor,
                        onCheckedChange = { viewModel.setDynamicColor(it) },
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ════════════════════════════════════════════════════════════
        // GESTION
        // ════════════════════════════════════════════════════════════
        SectionLabel("GESTION")

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = colors.Foreground,
        ) {
            Column {
                SettingsNavItem(
                    icon = Lucide.Grid2x2,
                    label = "Categorias",
                    onClick = onNavigateToCategories,
                )
                SettingsDivider()
                SettingsNavItem(
                    icon = Lucide.TrendingUp,
                    label = "Tasas de cambio",
                    onClick = onNavigateToExchangeRates,
                )
                SettingsDivider()
                SettingsNavItem(
                    icon = Lucide.Wallet,
                    label = "Presupuestos",
                    onClick = onNavigateToBudgets,
                )
                SettingsDivider()
                SettingsNavItem(
                    icon = Lucide.Landmark,
                    label = "Deudas",
                    onClick = onNavigateToDebts,
                )
                SettingsDivider()
                SettingsNavItem(
                    icon = Lucide.Tag,
                    label = "Titulos asociados",
                    onClick = onNavigateToAssociatedTitles,
                )
                SettingsDivider()
                SettingsNavItem(
                    icon = Lucide.Repeat,
                    label = "Transacciones recurrentes",
                    onClick = onNavigateToRecurring,
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // ════════════════════════════════════════════════════════════
        // DATOS
        // ════════════════════════════════════════════════════════════
        SectionLabel("DATOS")

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = colors.Foreground,
        ) {
            Column {
                SettingsNavItem(
                    icon = Lucide.FileSpreadsheet,
                    label = "Exportar CSV",
                    onClick = onNavigateToCsvExport,
                )
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

// ── Section label ───────────────────────────────────────────────
@Composable
private fun SectionLabel(text: String) {
    val colors = CeroFiaoDesign.colors
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = colors.TextSecondary,
        letterSpacing = 1.5.sp,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
    )
}

// ── Navigation item row ─────────────────────────────────────────
@Composable
private fun SettingsNavItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = colors.TextSecondary,
        )
        Spacer(Modifier.width(14.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = colors.TextPrimary,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Lucide.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = colors.TextSecondary.copy(alpha = 0.5f),
        )
    }
}

// ── Thin divider between nav items ──────────────────────────────
@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 20.dp),
        thickness = 0.5.dp,
        color = CeroFiaoDesign.colors.CardBorder.copy(alpha = 0.5f),
    )
}
