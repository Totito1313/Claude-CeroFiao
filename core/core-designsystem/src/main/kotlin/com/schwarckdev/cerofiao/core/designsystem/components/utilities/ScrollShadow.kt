package com.schwarckdev.cerofiao.core.designsystem.components.utilities

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Adds dynamic gradient shadows to scrollable content based on scroll position.
 * Replaces HeroUI's ScrollShadow component.
 */
@Composable
fun ScrollShadow(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    shadowHeight: Dp = 40.dp,
    color: Color = CeroFiaoDesign.colors.Background,
    content: @Composable () -> Unit
) {
    val showTopShadow by remember {
        derivedStateOf { scrollState.value > 0 }
    }
    val showBottomShadow by remember {
        derivedStateOf { scrollState.value < scrollState.maxValue }
    }

    ScrollShadowLayout(
        modifier = modifier,
        showTopShadow = showTopShadow,
        showBottomShadow = showBottomShadow,
        shadowHeight = shadowHeight,
        color = color,
        content = content
    )
}

/**
 * ScrollShadow overload for LazyListState.
 */
@Composable
fun ScrollShadow(
    listState: LazyListState,
    modifier: Modifier = Modifier,
    shadowHeight: Dp = 40.dp,
    color: Color = CeroFiaoDesign.colors.Background,
    content: @Composable () -> Unit
) {
    val showTopShadow by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }
    val showBottomShadow by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem == null || lastVisibleItem.index < listState.layoutInfo.totalItemsCount - 1
                    || lastVisibleItem.offset + lastVisibleItem.size > listState.layoutInfo.viewportEndOffset
        }
    }

    ScrollShadowLayout(
        modifier = modifier,
        showTopShadow = showTopShadow,
        showBottomShadow = showBottomShadow,
        shadowHeight = shadowHeight,
        color = color,
        content = content
    )
}

@Composable
private fun ScrollShadowLayout(
    modifier: Modifier,
    showTopShadow: Boolean,
    showBottomShadow: Boolean,
    shadowHeight: Dp,
    color: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.drawWithContent {
            drawContent()

            if (showTopShadow) {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(color, Color.Transparent),
                        startY = 0f,
                        endY = shadowHeight.toPx()
                    ),
                    size = Size(size.width, shadowHeight.toPx())
                )
            }

            if (showBottomShadow) {
                val startY = size.height - shadowHeight.toPx()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, color),
                        startY = startY,
                        endY = size.height
                    ),
                    topLeft = Offset(0f, startY),
                    size = Size(size.width, shadowHeight.toPx())
                )
            }
        }
    ) {
        content()
    }
}
