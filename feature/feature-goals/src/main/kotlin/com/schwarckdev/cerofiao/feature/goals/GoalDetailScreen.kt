package com.schwarckdev.cerofiao.feature.goals

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.ui.GlassCard
import com.schwarckdev.cerofiao.core.ui.GlassCardPadding

@Composable
fun GoalDetailScreen(
    onBack: () -> Unit,
    onEditGoal: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GoalDetailViewModel = hiltViewModel()
) {
    val t = CeroFiaoTheme.tokens
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val goal = uiState.goal

    Box(modifier = modifier.fillMaxSize().background(t.bg)) {
        if (goal == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF8A2BE2))
            return@Box
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(shape = CircleShape, color = t.iconBg) {
                    IconButton(onClick = onBack) {
                        Icon(CeroFiaoIcons.Back, contentDescription = "Volver", tint = t.text)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Surface(shape = CircleShape, color = t.iconBg) {
                    IconButton(onClick = { onEditGoal(viewModel.goalId) }) {
                        Icon(CeroFiaoIcons.Edit, contentDescription = "Editar", tint = t.text)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Surface(shape = CircleShape, color = t.iconBg) {
                    IconButton(onClick = {
                        viewModel.deleteGoal()
                        onBack()
                    }) {
                        Icon(CeroFiaoIcons.Delete, contentDescription = "Eliminar", tint = t.danger)
                    }
                }
            }

            // Body
            LazyColumn(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    val goalColor = goal.colorHex?.let { androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(it)) } ?: Color(0xFF8A2BE2)
                    GoalDetailHeader(
                        name = goal.name,
                        currentAmount = goal.currentAmountInUsd,
                        targetAmount = goal.targetAmount,
                        currency = goal.currencyCode,
                        iconName = goal.iconName,
                        color = goalColor,
                    )
                }

                item {
                    Text("Aportes Históricos", style = MaterialTheme.typography.titleMedium, color = t.text)
                }

                if (uiState.contributions.isEmpty()) {
                    item {
                        Text(
                            "Aún no hay aportes registrados para esta meta.",
                            color = t.textMuted,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    items(uiState.contributions, key = { it.id }) { contr ->
                        GlassCard(padding = GlassCardPadding.Small, modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = DateUtils.formatDisplayDate(contr.contributedAt),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = t.text
                                    )
                                    Text(
                                        text = if (contr.transactionId != null) "Vinculado a Transacción" else "Manual",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = t.textMuted
                                    )
                                }
                                Text(
                                    text = "+${CurrencyFormatter.format(contr.amount, contr.currencyCode, false)} ${contr.currencyCode}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = t.success
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun GoalDetailHeader(
    name: String,
    currentAmount: Double,
    targetAmount: Double,
    currency: String,
    iconName: String?,
    color: androidx.compose.ui.graphics.Color
) {
    val t = CeroFiaoTheme.tokens
    val progress = if (targetAmount > 0) (currentAmount / targetAmount).coerceIn(0.0, 1.0) else 0.0

    GlassCard(modifier = Modifier.fillMaxWidth(), padding = GlassCardPadding.Medium) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(CeroFiaoIcons.getCategoryIconRes(iconName ?: "Target")),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = name, style = MaterialTheme.typography.headlineSmall, color = t.text, fontWeight = FontWeight.Bold)
                    Text(text = "Meta: ${CurrencyFormatter.format(targetAmount, currency, false)} $currency", style = MaterialTheme.typography.bodyMedium, color = t.textMuted)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Column {
                    Text(text = "Ahorrado (equiv. USD)", style = MaterialTheme.typography.labelMedium, color = t.textMuted)
                    Text(
                        text = "$${CurrencyFormatter.format(currentAmount, "USD", false)}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.LinearProgressIndicator(
                progress = { progress.toFloat() },
                modifier = Modifier.fillMaxWidth().height(12.dp).clip(CircleShape),
                color = color,
                trackColor = color.copy(alpha = 0.2f),
            )
        }
    }
}
