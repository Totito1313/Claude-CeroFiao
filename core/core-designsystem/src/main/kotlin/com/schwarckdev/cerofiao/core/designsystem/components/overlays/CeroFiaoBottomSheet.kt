package com.schwarckdev.cerofiao.core.designsystem.components.overlays

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes

/**
 * Bottom sheet that slides up from the bottom with swipe-to-dismiss.
 * Replaces HeroUI's BottomSheet component.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CeroFiaoBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    shape: Shape = RoundedCornerShape(
        topStart = CeroFiaoShapes.BottomSheetRadius,
        topEnd = CeroFiaoShapes.BottomSheetRadius
    ),
    dragHandle: @Composable (() -> Unit)? = { CeroFiaoBottomSheetDragHandle() },
    content: @Composable ColumnScope.() -> Unit
) {
    val t = CeroFiaoDesign.colors

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = shape,
        containerColor = t.Surface,
        contentColor = t.TextPrimary,
        dragHandle = dragHandle,
        scrimColor = t.ShadowColor,
        content = content
    )
}

@Composable
fun CeroFiaoBottomSheetDragHandle() {
    Box(
        modifier = Modifier
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(CeroFiaoDesign.colors.CardBorder)
        )
    }
}
