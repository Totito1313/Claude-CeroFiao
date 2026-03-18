package com.SchwarckDev.CeroFiao

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.SchwarckDev.CeroFiao.navigation.CeroFiaoNavHost
import com.SchwarckDev.CeroFiao.navigation.TopLevelDestination
import androidx.compose.foundation.isSystemInDarkTheme
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.model.ThemeMode
import com.schwarckdev.cerofiao.feature.debt.navigateToAddDebt
import com.schwarckdev.cerofiao.feature.transactions.navigateToTransactionEntry
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val shortcutAction = intent?.action?.let { ShortcutAction.fromIntent(it) }

        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val preferences by viewModel.preferences.collectAsStateWithLifecycle()

            val isDark = when (preferences.themeMode) {
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }
            CeroFiaoTheme(
                darkTheme = isDark,
                dynamicColor = preferences.useDynamicColor,
            ) {
                CeroFiaoApp(
                    hasCompletedOnboarding = preferences.hasCompletedOnboarding,
                    shortcutAction = shortcutAction,
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}

enum class ShortcutAction {
    ADD_TRANSACTION,
    VIEW_BALANCE,
    ADD_DEBT;

    companion object {
        fun fromIntent(action: String): ShortcutAction? = when (action) {
            ACTION_ADD_TRANSACTION -> ADD_TRANSACTION
            ACTION_VIEW_BALANCE -> VIEW_BALANCE
            ACTION_ADD_DEBT -> ADD_DEBT
            else -> null
        }

        private const val ACTION_ADD_TRANSACTION = "com.SchwarckDev.CeroFiao.ACTION_ADD_TRANSACTION"
        private const val ACTION_VIEW_BALANCE = "com.SchwarckDev.CeroFiao.ACTION_VIEW_BALANCE"
        private const val ACTION_ADD_DEBT = "com.SchwarckDev.CeroFiao.ACTION_ADD_DEBT"
    }
}

@Composable
fun CeroFiaoApp(
    hasCompletedOnboarding: Boolean,
    shortcutAction: ShortcutAction? = null,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = TopLevelDestination.entries.any { dest ->
        currentDestination?.hasRoute(dest.route::class) == true
    }

    // Handle shortcut actions — wait until onboarding is loaded, then navigate once
    var shortcutHandled by remember { mutableStateOf(false) }
    LaunchedEffect(shortcutAction, hasCompletedOnboarding) {
        if (shortcutAction != null && hasCompletedOnboarding && !shortcutHandled) {
            shortcutHandled = true
            when (shortcutAction) {
                ShortcutAction.ADD_TRANSACTION -> {
                    navController.navigateToTransactionEntry()
                }
                ShortcutAction.VIEW_BALANCE -> {
                    // Dashboard is start destination, no navigation needed
                }
                ShortcutAction.ADD_DEBT -> {
                    navController.navigateToAddDebt()
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    TopLevelDestination.entries.forEach { destination ->
                        val selected = currentDestination?.hasRoute(destination.route::class) == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = destination.label,
                                )
                            },
                            label = { Text(destination.label) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        CeroFiaoNavHost(
            navController = navController,
            hasCompletedOnboarding = hasCompletedOnboarding,
            modifier = Modifier.padding(innerPadding),
        )
    }
}
