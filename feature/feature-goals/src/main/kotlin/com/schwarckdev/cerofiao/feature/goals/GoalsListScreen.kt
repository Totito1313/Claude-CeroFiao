package com.schwarckdev.cerofiao.feature.goals
import androidx.compose.foundation.layout.statusBarsPadding

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
import com.schwarckdev.cerofiao.core.ui.CeroFiaoFAB
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.common.CurrencyFormatter
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.model.SavingsGoal
import com.schwarckdev.cerofiao.core.ui.GlassCard

@Composable
fun GoalsListScreen(
    onAddGoal: () -> Unit,
    onGoalClick: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GoalsViewModel = hiltViewModel()
) {
    val t = CeroFiaoTheme.tokens
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize().background(t.bg).statusBarsPadding()) {
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
                Text(
                    text = "Objetivos",
                    style = MaterialTheme.typography.titleLarge,
                    color = t.text,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            // List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiState.goals.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No hay objetivos activos.\n¡Crea uno nuevo!",
                                color = t.textMuted,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(uiState.goals, key = { it.id }) { goal ->
                        GoalItem(goal = goal, onClick = { onGoalClick(goal.id) })
                    }
                }
            }
        }

        CeroFiaoFAB(
            icon = CeroFiaoIcons.Add,
            onClick = onAddGoal,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            contentDescription = "Nuevo objetivo"
        )
    }
}

@Composable
fun GoalItem(
    goal: SavingsGoal,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val t = CeroFiaoTheme.tokens
    val goalColor = goal.colorHex?.let { androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(it)) } ?: Color(0xFF8A2BE2)
    val progress = if (goal.targetAmount > 0) (goal.currentAmountInUsd / goal.targetAmount).coerceIn(0.0, 1.0) else 0.0

    GlassCard(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(goalColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(CeroFiaoIcons.getCategoryIconRes(goal.iconName ?: "Target")),
                        contentDescription = null,
                        tint = goalColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = goal.name, style = MaterialTheme.typography.titleMedium, color = t.text)
                    Text(
                        text = "Meta: ${CurrencyFormatter.format(goal.targetAmount, goal.currencyCode, false)} ${goal.currencyCode}",
                        style = MaterialTheme.typography.bodySmall,
                        color = t.textMuted
                    )
                }
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = goalColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = goalColor,
                trackColor = goalColor.copy(alpha = 0.2f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "${CurrencyFormatter.format(goal.currentAmountInUsd, "USD", false)} USD",
                    style = MaterialTheme.typography.bodySmall,
                    color = t.textSecondary
                )
            }
        }
    }
}

