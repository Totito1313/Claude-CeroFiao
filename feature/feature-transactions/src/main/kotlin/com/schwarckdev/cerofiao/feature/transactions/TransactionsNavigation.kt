package com.schwarckdev.cerofiao.feature.transactions

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object TransactionListRoute

@Serializable
object TransactionEntryRoute

@Serializable
object TransferRoute

fun NavGraphBuilder.transactionListScreen(
    onAddTransaction: () -> Unit,
    onTransactionClick: (String) -> Unit,
) {
    composable<TransactionListRoute> {
        TransactionListScreen(
            onAddTransaction = onAddTransaction,
            onTransactionClick = onTransactionClick,
        )
    }
}

fun NavGraphBuilder.transactionEntryScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
) {
    composable<TransactionEntryRoute> {
        TransactionEntryScreen(
            onBack = onBack,
            onSaved = onSaved,
        )
    }
}

fun NavGraphBuilder.transferScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
) {
    composable<TransferRoute> {
        TransferScreen(
            onBack = onBack,
            onSaved = onSaved,
        )
    }
}

fun NavController.navigateToTransactionList() {
    navigate(TransactionListRoute)
}

fun NavController.navigateToTransactionEntry() {
    navigate(TransactionEntryRoute)
}

fun NavController.navigateToTransfer() {
    navigate(TransferRoute)
}
