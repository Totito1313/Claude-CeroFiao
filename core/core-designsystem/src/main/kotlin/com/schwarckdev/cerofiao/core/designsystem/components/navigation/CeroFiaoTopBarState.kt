package com.schwarckdev.cerofiao.core.designsystem.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class TopBarMenuItem(
    val text: String,
    val onClick: () -> Unit,
    val icon: ImageVector? = null,
    val color: Color = Color.Unspecified
)

@Stable
class CeroFiaoTopBarState {
    var variant by mutableStateOf(TopBarVariant.Home)
    var title by mutableStateOf("")
    var subtitle by mutableStateOf<String?>(null)
    var showUserSubtitle by mutableStateOf(false)
    var userName by mutableStateOf("Usuario")
    var duration by mutableStateOf("00:00")
    var heartRate by mutableStateOf("0")
    var onBackClick by mutableStateOf<(() -> Unit)?>(null)
    var onMenuClick by mutableStateOf<(() -> Unit)?>(null)
    var onActionClick by mutableStateOf<(() -> Unit)?>(null)
    var onSearchClick by mutableStateOf<(() -> Unit)?>(null)
    var onFilterClick by mutableStateOf<(() -> Unit)?>(null)
    var isVisible by mutableStateOf(true)
    var menuItems by mutableStateOf<List<TopBarMenuItem>>(emptyList())
    var rightIcon by mutableStateOf<Int?>(null)
    var rightIconVector by mutableStateOf<ImageVector?>(null)
    var profilePicture by mutableStateOf<Any?>(null)
    var scrollOffset by mutableFloatStateOf(0f)

    fun update(
        variant: TopBarVariant,
        title: String = "",
        subtitle: String? = null,
        showUserSubtitle: Boolean = false,
        userName: String = "Usuario",
        duration: String = "00:00",
        heartRate: String = "0",
        onBackClick: (() -> Unit)? = null,
        onMenuClick: (() -> Unit)? = null,
        onActionClick: (() -> Unit)? = null,
        onSearchClick: (() -> Unit)? = null,
        onFilterClick: (() -> Unit)? = null,
        isVisible: Boolean = true,
        menuItems: List<TopBarMenuItem> = emptyList(),
        rightIcon: Int? = null,
        rightIconVector: ImageVector? = null,
        profilePicture: Any? = null
    ) {
        this.variant = variant
        this.title = title
        this.subtitle = subtitle
        this.showUserSubtitle = showUserSubtitle
        this.userName = userName
        this.duration = duration
        this.heartRate = heartRate
        this.onBackClick = onBackClick
        this.onMenuClick = onMenuClick
        this.onActionClick = onActionClick
        this.onSearchClick = onSearchClick
        this.onFilterClick = onFilterClick
        this.isVisible = isVisible
        this.menuItems = menuItems
        this.rightIcon = rightIcon
        this.rightIconVector = rightIconVector
        this.profilePicture = profilePicture
    }

    fun hide() {
        this.isVisible = false
    }
}


val LocalTopBarState = staticCompositionLocalOf<CeroFiaoTopBarState> {
    error("No CeroFiaoTopBarState provided")
}

@Composable
fun ConfigureTopBar(
    variant: TopBarVariant = TopBarVariant.Standard,
    title: String = "",
    subtitle: String? = null,
    showUserSubtitle: Boolean = false,
    userName: String = "Usuario",
    duration: String = "00:00",
    heartRate: String = "0",
    rightIcon: Int? = null,
    rightIconVector: ImageVector? = null,
    profilePicture: Any? = null,
    menuItems: List<TopBarMenuItem> = emptyList(),
    onActionClick: (() -> Unit)? = null,
    onSearchClick: (() -> Unit)? = null,
    onFilterClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    isVisible: Boolean = true,
    enabled: Boolean = true
) {
    val topBarState = LocalTopBarState.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    // Use DisposableEffect + LifecycleEventObserver to only update the TopBar
    // when the screen is RESUMED. This prevents the exiting screen from
    // overwriting the incoming screen's TopBar during navigation transitions.
    DisposableEffect(
        lifecycleOwner,
        enabled, variant, title, subtitle, showUserSubtitle, userName,
        duration, heartRate, isVisible, rightIcon, rightIconVector, profilePicture
    ) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_START && enabled) {
                topBarState.update(
                    variant = variant,
                    title = title,
                    subtitle = subtitle,
                    showUserSubtitle = showUserSubtitle,
                    userName = userName,
                    duration = duration,
                    heartRate = heartRate,
                    onBackClick = onBackClick,
                    onMenuClick = onMenuClick,
                    onActionClick = onActionClick,
                    onSearchClick = onSearchClick,
                    onFilterClick = onFilterClick,
                    isVisible = isVisible,
                    menuItems = menuItems,
                    rightIcon = rightIcon,
                    rightIconVector = rightIconVector,
                    profilePicture = profilePicture
                )
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // Also update immediately if already started (first composition or param change)
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(
                androidx.lifecycle.Lifecycle.State.STARTED
            ) && enabled
        ) {
            topBarState.update(
                variant = variant,
                title = title,
                subtitle = subtitle,
                showUserSubtitle = showUserSubtitle,
                userName = userName,
                duration = duration,
                heartRate = heartRate,
                onBackClick = onBackClick,
                onMenuClick = onMenuClick,
                onActionClick = onActionClick,
                onSearchClick = onSearchClick,
                onFilterClick = onFilterClick,
                isVisible = isVisible,
                menuItems = menuItems,
                rightIcon = rightIcon,
                rightIconVector = rightIconVector,
                profilePicture = profilePicture
            )
        }

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
