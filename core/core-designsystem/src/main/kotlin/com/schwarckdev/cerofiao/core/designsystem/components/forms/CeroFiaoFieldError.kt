package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.Lucide
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Validation error message with animated entrance/exit.
 * Replaces HeroUI's FieldError component.
 */
@Composable
fun CeroFiaoFieldError(
    message: String?,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true
) {
    AnimatedVisibility(
        visible = message != null,
        enter = fadeIn() + slideInVertically { -it / 2 },
        exit = fadeOut() + slideOutVertically { -it / 2 }
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showIcon) {
                Icon(
                    imageVector = Lucide.CircleAlert,
                    contentDescription = null,
                    tint = CeroFiaoDesign.colors.Error,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = message ?: "",
                color = CeroFiaoDesign.colors.Error,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}
