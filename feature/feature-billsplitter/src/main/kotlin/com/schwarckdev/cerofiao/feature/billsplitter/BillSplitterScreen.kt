package com.schwarckdev.cerofiao.feature.billsplitter
import com.schwarckdev.cerofiao.core.ui.navigation.TopBarVariant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.ui.GlassCard
import com.schwarckdev.cerofiao.core.ui.GlassCardPadding
// removed import
import com.schwarckdev.cerofiao.core.ui.MoneyAmountInput
import com.schwarckdev.cerofiao.core.ui.CeroFiaoFAB
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButton
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButtonVariant
import com.schwarckdev.cerofiao.core.ui.CeroFiaoTextField
import com.schwarckdev.cerofiao.core.ui.CeroFiaoDialog

@Composable
fun BillSplitterScreen(
    onBack: () -> Unit,
    viewModel: BillSplitterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val t = CeroFiaoTheme.tokens

    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(t.bg).padding(top = 90.dp)
    ) {
        com.schwarckdev.cerofiao.core.ui.navigation.ConfigureTopBar(
            variant = com.schwarckdev.cerofiao.core.ui.navigation.TopBarVariant.Detail,
            title = "Divisor de Cuentas",
            onBackClick = onBack
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Main Input Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                padding = GlassCardPadding.Medium
            ) {
                Column {
                    Text(
                        text = "Monto Total",
                        color = t.textMuted,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    MoneyAmountInput(
                        amount = state.totalAmountStr,
                        onAmountChange = viewModel::onTotalAmountChanged,
                        currency = state.baseCurrency,
                        onCurrencyClick = {
                            viewModel.onCurrencyChanged(if (state.baseCurrency == "USD") "VES" else "USD")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tasa BCV: ${String.format("%.2f", state.bcvRate)} Bs / \$",
                        color = t.textGhost,
                        fontSize = 12.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "Participantes (${state.participants.size})",
                color = t.text,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            
            if (state.unallocatedBase > 0.01 || state.unallocatedBase < -0.01) {
                Text(
                    text = "No asignado: ${String.format("%.2f", state.unallocatedBase)} ${state.baseCurrency}",
                    color = t.danger,
                    fontSize = 12.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(state.participants, key = { it.id }) { participant ->
                    ParticipantRow(
                        participant = participant,
                        baseCurrency = state.baseCurrency,
                        onUpdateType = { viewModel.updateParticipantType(participant.id, it) },
                        onUpdateFixed = { viewModel.updateParticipantFixed(participant.id, it) },
                        onUpdatePercentage = { viewModel.updateParticipantPercentage(participant.id, it) },
                        onRemove = { viewModel.removeParticipant(participant.id) }
                    )
                }
            }
        }
        
        if (showAddDialog) {
            AddParticipantDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { 
                    viewModel.addParticipant(it)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AddParticipantDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    val t = CeroFiaoTheme.tokens
    
    CeroFiaoDialog(
        onDismissRequest = onDismiss,
        title = "Agregar participante",
        text = "Ingresa el nombre del participante",
        content = {
            CeroFiaoTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = "Ej. Ana, Juan...",
                singleLine = true
            )
        },
        confirmButton = {
            CeroFiaoButton(
                text = "Agregar",
                onClick = { onAdd(name) },
                variant = CeroFiaoButtonVariant.Primary,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            )
        },
        dismissButton = {
            CeroFiaoButton(
                text = "Cancelar",
                onClick = onDismiss,
                variant = CeroFiaoButtonVariant.Text,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    )
}

@Composable
fun ParticipantRow(
    participant: SplitParticipant,
    baseCurrency: String,
    onUpdateType: (SplitType) -> Unit,
    onUpdateFixed: (Double) -> Unit,
    onUpdatePercentage: (Double) -> Unit,
    onRemove: () -> Unit
) {
    val t = CeroFiaoTheme.tokens
    
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        padding = GlassCardPadding.Medium
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = participant.name,
                    color = t.text,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                    Icon(CeroFiaoIcons.Close, "Eliminar", tint = t.textFaint)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SplitTypeSelector(
                    selected = participant.splitType == SplitType.EQUAL,
                    label = "Partes Iguales",
                    onClick = { onUpdateType(SplitType.EQUAL) },
                    modifier = Modifier.weight(1f)
                )
                SplitTypeSelector(
                    selected = participant.splitType == SplitType.PERCENTAGE,
                    label = "Porcentaje",
                    onClick = { onUpdateType(SplitType.PERCENTAGE) },
                    modifier = Modifier.weight(1f)
                )
                SplitTypeSelector(
                    selected = participant.splitType == SplitType.FIXED,
                    label = "Fijo ($)",
                    onClick = { onUpdateType(SplitType.FIXED) },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            when (participant.splitType) {
                SplitType.FIXED -> {
            CeroFiaoTextField(
                        value = if (participant.fixedAmount == 0.0) "" else participant.fixedAmount.toString(),
                        onValueChange = { onUpdateFixed(it.toDoubleOrNull() ?: 0.0) },
                        label = "Monto",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                SplitType.PERCENTAGE -> {
                    CeroFiaoTextField(
                        value = if (participant.percentage == 0.0) "" else participant.percentage.toString(),
                        onValueChange = { onUpdatePercentage(it.toDoubleOrNull() ?: 0.0) },
                        label = "Porcentaje (%)",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                SplitType.EQUAL -> {
                    // Equal is automatically calculated
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            // Totals
            Surface(
                color = t.surface,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total a pagar:", color = t.textMuted, fontSize = 14.sp)
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = String.format("%.2f %s", participant.finalAmountBaseCurrency, baseCurrency),
                            color = Color(0xFF8A2BE2),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = String.format(
                                "≈ %s",
                                if (baseCurrency == "USD") String.format("%.2f VES", participant.equivalentVes)
                                else String.format("%.2f USD", participant.equivalentUsd)
                            ),
                            color = t.textGhost,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SplitTypeSelector(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val t = CeroFiaoTheme.tokens
    val bgColor = if (selected) Color(0xFF8A2BE2) else t.surface
    val txtColor = if (selected) androidx.compose.ui.graphics.Color.White else t.textMuted
    
    Surface(
        onClick = onClick,
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.height(36.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(label, color = txtColor, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}

