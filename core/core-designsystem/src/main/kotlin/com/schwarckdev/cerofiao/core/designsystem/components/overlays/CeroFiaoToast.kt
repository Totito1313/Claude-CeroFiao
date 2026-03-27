package com.schwarckdev.cerofiao.core.designsystem.components.overlays

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.TriangleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.CircleX
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.ComponentSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

enum class ToastVariant {
    Default,
    Accent,
    Success,
    Warning,
    Danger
}

enum class ToastPosition {
    Top,
    Bottom
}

data class ToastData(
    val id: String = UUID.randomUUID().toString(),
    val message: String,
    val variant: ToastVariant = ToastVariant.Default,
    val icon: ImageVector? = null,
    val duration: Long = 3000L,
    val action: ToastAction? = null
)

data class ToastAction(
    val label: String,
    val onClick: () -> Unit
)

class CeroFiaoToastHostState {
    internal val toasts = mutableStateListOf<ToastData>()
    private val mutex = Mutex()

    suspend fun show(data: ToastData) {
        mutex.withLock {
            toasts.add(data)
        }
    }

    fun dismiss(id: String) {
        toasts.removeAll { it.id == id }
    }

    internal fun dismissFirst() {
        if (toasts.isNotEmpty()) toasts.removeAt(0)
    }
}

@Composable
fun rememberCeroFiaoToastHostState(): CeroFiaoToastHostState {
    return remember { CeroFiaoToastHostState() }
}

/**
 * Host for displaying toast notifications.
 * Replaces HeroUI's Toast component.
 */
@Composable
fun CeroFiaoToastHost(
    hostState: CeroFiaoToastHostState,
    modifier: Modifier = Modifier,
    position: ToastPosition = ToastPosition.Bottom,
    maxVisibleToasts: Int = 3
) {
    val scope = rememberCoroutineScope()
    val visibleToasts = hostState.toasts.takeLast(maxVisibleToasts)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = if (position == ToastPosition.Top) Alignment.TopCenter else Alignment.BottomCenter
    ) {
        Column(
            verticalArrangement = if (position == ToastPosition.Top) {
                Arrangement.spacedBy(8.dp)
            } else {
                Arrangement.spacedBy(8.dp, Alignment.Bottom)
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            visibleToasts.forEach { toast ->
                key(toast.id) {
                    CeroFiaoToastItem(
                        data = toast,
                        position = position,
                        onDismiss = { hostState.dismiss(toast.id) }
                    )

                    LaunchedEffect(toast.id) {
                        delay(toast.duration)
                        hostState.dismiss(toast.id)
                    }
                }
            }
        }
    }
}

@Composable
private fun key(id: String, content: @Composable () -> Unit) {
    content()
}

@Composable
private fun CeroFiaoToastItem(
    data: ToastData,
    position: ToastPosition,
    onDismiss: () -> Unit
) {
    val t = CeroFiaoDesign.colors

    val colors = when (data.variant) {
        ToastVariant.Default -> t.Surface to t.TextPrimary
        ToastVariant.Accent -> t.AccentSoft to t.Primary
        ToastVariant.Success -> t.SuccessSoft to t.Success
        ToastVariant.Warning -> t.WarningSoft to t.Warning
        ToastVariant.Danger -> t.DangerSoft to t.Error
    }

    val effectiveIcon = data.icon ?: when (data.variant) {
        ToastVariant.Default -> Lucide.Info
        ToastVariant.Accent -> Lucide.Info
        ToastVariant.Success -> Lucide.CircleCheck
        ToastVariant.Warning -> Lucide.TriangleAlert
        ToastVariant.Danger -> Lucide.CircleX
    }

    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + if (position == ToastPosition.Top) {
            slideInVertically { -it }
        } else {
            slideInVertically { it }
        },
        exit = fadeOut() + if (position == ToastPosition.Top) {
            slideOutVertically { -it }
        } else {
            slideOutVertically { it }
        }
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = ComponentSize.toastMaxWidth)
                .clip(RoundedCornerShape(CeroFiaoDesign.radius.md))
                .background(colors.first)
                .draggable(
                    orientation = if (position == ToastPosition.Top) Orientation.Vertical else Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        val threshold = 50f
                        if ((position == ToastPosition.Top && delta < -threshold) ||
                            (position == ToastPosition.Bottom && delta > threshold)
                        ) {
                            onDismiss()
                        }
                    }
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = effectiveIcon,
                contentDescription = null,
                tint = colors.second,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = data.message,
                color = colors.second,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            if (data.action != null) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = data.action.label,
                    color = colors.second,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}
