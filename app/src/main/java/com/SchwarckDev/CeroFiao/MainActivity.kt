package com.SchwarckDev.CeroFiao

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.SchwarckDev.CeroFiao.navigation.CeroFiaoNavHost
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.CeroFiaoCornerMenu
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.CeroFiaoFloatingNavBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.CeroFiaoTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.CeroFiaoTopBarState
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.LocalTopBarState
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.designsystem.theme.FloatMenuConfig
import com.schwarckdev.cerofiao.core.designsystem.theme.ThemePreferencesManager
import com.schwarckdev.cerofiao.core.designsystem.theme.UserPreferences as ThemeUserPreferences
import com.schwarckdev.cerofiao.core.model.ThemeMode
import com.schwarckdev.cerofiao.feature.dashboard.DashboardRoute
import com.schwarckdev.cerofiao.feature.dashboard.navigateToAdminSettings
import com.schwarckdev.cerofiao.feature.debt.navigateToAddDebt
import com.schwarckdev.cerofiao.feature.debt.navigateToDebtList
import com.schwarckdev.cerofiao.feature.accounts.navigateToAccountList
import com.schwarckdev.cerofiao.feature.exchangerates.navigateToExchangeRates
import com.schwarckdev.cerofiao.feature.settings.navigateToSettings
import com.schwarckdev.cerofiao.feature.transactions.navigateToTransactionEntry
import com.schwarckdev.cerofiao.feature.transactions.navigateToTransactionList
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val shortcutAction = intent?.action?.let { ShortcutAction.fromIntent(it) }

        val themePreferencesManager = ThemePreferencesManager.getInstance(applicationContext)

        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val preferences by viewModel.preferences.collectAsStateWithLifecycle()
            val themePrefs by themePreferencesManager.preferencesFlow
                .collectAsStateWithLifecycle(initialValue = ThemeUserPreferences())

            val isDark = when (preferences.themeMode) {
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            val accentColor = if (isDark) themePrefs.accentPreset.darkColor else themePrefs.accentPreset.lightColor

            CeroFiaoTheme(
                darkTheme = isDark,
                dynamicColor = preferences.useDynamicColor,
                titleFontFamily = themePrefs.titleFont.toFontFamily(),
                bodyFontFamily = themePrefs.bodyFont.toFontFamily(),
                subtitleFontFamily = themePrefs.subtitleFont.toFontFamily(),
                accentColor = accentColor,
                shadowConfig = themePrefs.shadowConfig,
                glassConfig = themePrefs.glassConfig,
                cardConfig = themePrefs.cardConfig,
                colorOverrides = themePrefs.colorOverrides,
            ) {
                CeroFiaoApp(
                    hasCompletedOnboarding = preferences.hasCompletedOnboarding,
                    shortcutAction = shortcutAction,
                    floatMenuConfig = themePrefs.floatMenuConfig,
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
    floatMenuConfig: FloatMenuConfig = FloatMenuConfig.Default,
) {
    val navController = rememberNavController()

    var shortcutHandled by remember { mutableStateOf(false) }
    LaunchedEffect(shortcutAction, hasCompletedOnboarding) {
        if (shortcutAction != null && hasCompletedOnboarding && !shortcutHandled) {
            shortcutHandled = true
            when (shortcutAction) {
                ShortcutAction.ADD_TRANSACTION -> navController.navigateToTransactionEntry()
                ShortcutAction.VIEW_BALANCE -> { /* Dashboard is start */ }
                ShortcutAction.ADD_DEBT -> navController.navigateToAddDebt()
            }
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Tab Mapping
    val activeTab = when {
        currentRoute?.contains("DashboardRoute") == true -> 0
        currentRoute?.contains("AccountListRoute") == true -> 1
        currentRoute?.contains("TransactionListRoute") == true -> 2
        currentRoute?.contains("ExchangeRateRoute") == true -> 3
        else -> -1
    }

    val isTopLevelRoute = activeTab != -1 || currentRoute?.contains("MoreRoute") == true

    var isMenuOpen by remember { mutableStateOf(false) }
    val topBarState = remember { CeroFiaoTopBarState() }
    val hazeState = remember { HazeState() }

    CompositionLocalProvider(
        LocalTopBarState provides topBarState,
    ) {
        Scaffold(
            topBar = {
                CeroFiaoTopBar(
                    state = topBarState,
                    hazeState = hazeState
                )
            },
            bottomBar = {
                if (isTopLevelRoute && hasCompletedOnboarding) {
                    Box(modifier = Modifier.offset(y = 12.dp)) {
                        CeroFiaoFloatingNavBar(
                            activeTab = activeTab,
                            isMenuOpen = isMenuOpen,
                            onTabSelected = { tabIndex ->
                                when (tabIndex) {
                                    0 -> navController.navigate(DashboardRoute)
                                    1 -> navController.navigateToAccountList()
                                    2 -> navController.navigateToTransactionList()
                                    3 -> navController.navigateToExchangeRates()
                                }
                            },
                            onMenuClick = { isMenuOpen = !isMenuOpen },
                            hazeState = hazeState,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                CeroFiaoNavHost(
                    navController = navController,
                    hasCompletedOnboarding = hasCompletedOnboarding,
                    modifier = Modifier
                        .fillMaxSize()
                        .haze(state = hazeState)
                )

                CeroFiaoCornerMenu(
                    hazeState = hazeState,
                    visible = isMenuOpen && isTopLevelRoute,
                    onMenuItemLongClick = { item ->
                        isMenuOpen = false
                        if (item == "Ajustes") navController.navigateToAdminSettings()
                    },
                    onMenuItemClick = { item ->
                        isMenuOpen = false
                        when (item) {
                            "Cuenta" -> navController.navigateToAccountList()
                            "Tasas" -> navController.navigateToExchangeRates()
                            "Deudas" -> navController.navigateToDebtList()
                            "Ajustes" -> navController.navigateToSettings()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            end = 16.dp,
                            bottom = floatMenuConfig.cornerMenuOffsetY
                        )
                )
            }
        }
    }
}
