package com.schwarckdev.cerofiao.feature.transactions

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object TransactionListRoute

@Serializable
data class TransactionEntryRoute(val transactionId: String? = null)

@Serializable
data class TransactionDetailRoute(val transactionId: String)

@Serializable
object TransferRoute

@Serializable
object TransactionActivityRoute

fun NavGraphBuilder.transactionListScreen(
    onAddTransaction: () -> Unit,
    onTransactionClick: (String) -> Unit,
    onActivityClick: () -> Unit = {},
) {
    composable<TransactionListRoute> {
        TransactionListScreen(
            onAddTransaction = onAddTransaction,
            onTransactionClick = onTransactionClick,
            onActivityClick = onActivityClick,
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

fun NavGraphBuilder.transactionDetailScreen(
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
) {
    composable<TransactionDetailRoute> {
        TransactionDetailScreen(
            onBack = onBack,
            onEdit = onEdit,
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

fun NavController.navigateToTransactionEntry(transactionId: String? = null) {
    navigate(TransactionEntryRoute(transactionId))
}

fun NavController.navigateToTransactionDetail(transactionId: String) {
    navigate(TransactionDetailRoute(transactionId))
}

fun NavController.navigateToTransfer() {
    navigate(TransferRoute)
}

fun NavGraphBuilder.transactionActivityScreen(
    onBack: () -> Unit,
) {
    composable<TransactionActivityRoute> {
        TransactionActivityScreen(onBack = onBack)
    }
}

fun NavController.navigateToTransactionActivity() {
    navigate(TransactionActivityRoute)
}
