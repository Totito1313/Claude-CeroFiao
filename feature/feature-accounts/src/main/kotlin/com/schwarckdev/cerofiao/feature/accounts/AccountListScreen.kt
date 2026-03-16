package com.schwarckdev.cerofiao.feature.accounts

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.ui.AccountCard
import com.schwarckdev.cerofiao.core.ui.EmptyState
import com.schwarckdev.cerofiao.core.ui.MoneyText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountListScreen(
    onAccountClick: (String) -> Unit,
    onAddAccount: () -> Unit,
    onTransfer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Cuentas")
                },
                actions = {
                    IconButton(onClick = onTransfer) {
                        Icon(
                            imageVector = CeroFiaoIcons.Transfer,
                            contentDescription = "Transferir",
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddAccount) {
                Icon(
                    imageVector = CeroFiaoIcons.Add,
                    contentDescription = "Agregar cuenta",
                )
            }
        },
    ) { innerPadding ->
        if (uiState.accounts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                EmptyState(
                    icon = CeroFiaoIcons.Accounts,
                    title = "Sin cuentas",
                    description = "Agrega tu primera cuenta",
                    actionLabel = "Agregar cuenta",
                    onAction = onAddAccount,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    BalanceSummarySection(
                        totalBalance = uiState.totalBalance,
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(
                    items = uiState.accounts,
                    key = { it.id },
                ) { account ->
                    AccountCard(
                        account = account,
                        onClick = { onAccountClick(account.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun BalanceSummarySection(
    totalBalance: Map<String, Double>,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Balance total",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            totalBalance.forEach { (currencyCode, amount) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = currencyCode,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                    )
                    MoneyText(
                        amount = amount,
                        currencyCode = currencyCode,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
    }
}
