package com.schwarckdev.cerofiao.feature.accounts

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
object AccountListRoute

@Serializable
data class AccountDetailRoute(val accountId: String)

@Serializable
object AddAccountRoute

fun NavGraphBuilder.accountListScreen(
    onAccountClick: (String) -> Unit,
    onAddAccount: () -> Unit,
    onTransfer: () -> Unit,
) {
    composable<AccountListRoute> {
        AccountListScreen(
            onAccountClick = onAccountClick,
            onAddAccount = onAddAccount,
            onTransfer = onTransfer,
        )
    }
}

fun NavGraphBuilder.addAccountScreen(
    onBack: () -> Unit,
    onAccountCreated: () -> Unit,
) {
    composable<AddAccountRoute> {
        AddAccountScreen(
            onBack = onBack,
            onAccountCreated = onAccountCreated,
        )
    }
}

fun NavGraphBuilder.accountDetailScreen(
    onBack: () -> Unit,
) {
    composable<AccountDetailRoute> {
        AccountDetailScreen(onBack = onBack)
    }
}

fun NavController.navigateToAccountList() {
    navigate(AccountListRoute)
}

fun NavController.navigateToAddAccount() {
    navigate(AddAccountRoute)
}

fun NavController.navigateToAccountDetail(accountId: String) {
    navigate(AccountDetailRoute(accountId))
}
