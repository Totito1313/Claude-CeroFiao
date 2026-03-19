package com.SchwarckDev.CeroFiao.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme

private data class MenuGroup(
    val title: String,
    val items: List<MenuItem>,
)

private data class MenuItem(
    val icon: ImageVector,
    val label: String,
    val description: String,
    val color: Color,
    val useGradient: Boolean = false,
)

@Composable
fun MoreScreen(
    onNavigateToAccounts: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToAlcancia: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val t = CeroFiaoTheme.tokens

    val menuGroups = listOf(
        MenuGroup(
            title = "FINANZAS",
            items = listOf(
                MenuItem(CeroFiaoIcons.Accounts, "Cuentas", "Patrimonio y transferencias", Color(0xFF00FF66)),
                MenuItem(CeroFiaoIcons.Analytics, "Analytics", "Estadísticas y presupuestos", Color(0xFF8A2BE2)),
                MenuItem(CeroFiaoIcons.Savings, "La Alcancía", "Metas de ahorro", Color(0xFFFF6B00)),
            ),
        ),
        MenuGroup(
            title = "SMART FEATURES",
            items = listOf(
                MenuItem(CeroFiaoIcons.AIChat, "Asistente IA", "Chat inteligente financiero", Color(0xFF8A2BE2), useGradient = true),
                MenuItem(CeroFiaoIcons.ScanReceipt, "Escanear Recibo", "OCR para registro rápido", Color(0xFF00D4FF)),
            ),
        ),
        MenuGroup(
            title = "GENERAL",
            items = listOf(
                MenuItem(CeroFiaoIcons.Settings, "Ajustes", "Preferencias y configuración", Color(0xFF8E8E93)),
            ),
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 100.dp),
    ) {
        // Header
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            Text(
                text = "Más",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = t.text,
            )
        }

        // Menu groups
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            menuGroups.forEach { group ->
                Column {
                    Text(
                        text = group.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = t.textMuted,
                        letterSpacing = 0.48.sp,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
                    )

                    Surface(
                        shape = RoundedCornerShape(CeroFiaoShapes.CardRadius),
                        color = t.surface,
                        border = BorderStroke(1.dp, t.surfaceBorder),
                    ) {
                        Column {
                            group.items.forEachIndexed { index, item ->
                                val onClick: () -> Unit = when (item.label) {
                                    "Cuentas" -> onNavigateToAccounts
                                    "Analytics" -> onNavigateToAnalytics
                                    "La Alcancía" -> onNavigateToAlcancia
                                    "Ajustes" -> onNavigateToSettings
                                    else -> {{}}
                                }

                                MoreMenuItem(
                                    icon = item.icon,
                                    label = item.label,
                                    description = item.description,
                                    color = item.color,
                                    useGradient = item.useGradient,
                                    onClick = onClick,
                                )

                                if (index < group.items.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        thickness = 1.dp,
                                        color = t.divider,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoreMenuItem(
    icon: ImageVector,
    label: String,
    description: String,
    color: Color,
    useGradient: Boolean,
    onClick: () -> Unit,
) {
    val t = CeroFiaoTheme.tokens

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Icon container
        Surface(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(12.dp),
            color = if (useGradient) Color.Transparent else color.copy(alpha = 0.08f),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp),
                    tint = if (useGradient) Color.White else color,
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Text
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = t.text,
            )
            Text(
                text = description,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = t.textMuted,
            )
        }

        Icon(
            imageVector = CeroFiaoIcons.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = t.textFaint,
        )
    }
}
