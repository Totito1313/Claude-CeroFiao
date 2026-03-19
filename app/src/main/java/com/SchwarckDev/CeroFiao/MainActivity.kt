package com.SchwarckDev.CeroFiao

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    // Handle shortcut actions
    var shortcutHandled by remember { mutableStateOf(false) }
    LaunchedEffect(shortcutAction, hasCompletedOnboarding) {
        if (shortcutAction != null && hasCompletedOnboarding && !shortcutHandled) {
            shortcutHandled = true
            when (shortcutAction) {
                ShortcutAction.ADD_TRANSACTION -> {
                    navController.navigateToTransactionEntry()
                }
                ShortcutAction.VIEW_BALANCE -> { /* Dashboard is start */ }
                ShortcutAction.ADD_DEBT -> {
                    navController.navigateToAddDebt()
                }
            }
        }
    }

    val t = CeroFiaoTheme.tokens

    Box(modifier = Modifier.fillMaxSize()) {
        CeroFiaoNavHost(
            navController = navController,
            hasCompletedOnboarding = hasCompletedOnboarding,
            modifier = Modifier.fillMaxSize(),
        )

        // Floating bottom navigation bar — Figma style
        AnimatedVisibility(
            visible = showBottomBar,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .navigationBarsPadding(),
            ) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = t.navBg,
                    border = BorderStroke(1.dp, t.navBorder),
                    shadowElevation = 8.dp,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        TopLevelDestination.entries.forEach { destination ->
                            val selected = currentDestination?.hasRoute(destination.route::class) == true
                            val activeColor = t.success
                            val inactiveColor = t.textMuted

                            Surface(
                                onClick = {
                                    navController.navigate(destination.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                shape = RoundedCornerShape(16.dp),
                                color = androidx.compose.ui.graphics.Color.Transparent,
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(2.dp),
                                ) {
                                    Icon(
                                        imageVector = destination.icon,
                                        contentDescription = destination.label,
                                        modifier = Modifier.size(22.dp),
                                        tint = if (selected) activeColor else inactiveColor,
                                    )
                                    Text(
                                        text = destination.label,
                                        fontSize = 10.sp,
                                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (selected) activeColor else t.textFaint,
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
