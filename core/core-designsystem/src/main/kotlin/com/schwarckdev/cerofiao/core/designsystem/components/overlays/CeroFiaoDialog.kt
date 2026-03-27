package com.schwarckdev.cerofiao.core.designsystem.components.overlays

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Enhanced dialog with animated transitions.
 * Replaces HeroUI's Dialog component and enhances original CeroFiaoDialog.
 */
@Composable
fun CeroFiaoDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    content: @Composable () -> Unit
) {
    val t = CeroFiaoDesign.colors

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside
        )
    ) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(24.dp),
            color = t.Surface,
            tonalElevation = 6.dp
        ) {
            content()
        }
    }
}

/**
 * Convenience overload preserving the original CeroFiaoDialog API.
 */
@Composable
fun CeroFiaoDialog(
    onDismissRequest: () -> Unit,
    title: String,
    text: String,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null
) {
    CeroFiaoDialog(onDismissRequest = onDismissRequest) {
        val t = CeroFiaoDesign.colors

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = t.TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                color = t.TextSecondary,
                lineHeight = 20.sp
            )
            if (content != null) {
                Spacer(modifier = Modifier.height(16.dp))
                content()
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                dismissButton?.invoke()
                Spacer(modifier = Modifier.width(8.dp))
                confirmButton()
            }
        }
    }
}
