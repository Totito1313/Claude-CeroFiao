package com.schwarckdev.cerofiao.core.ui.navigation

import com.schwarckdev.cerofiao.core.designsystem.theme.*
import dev.chrisbanes.haze.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalBlurEnabled
import androidx.compose.ui.draw.shadow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
// Import ignored
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild

/**
 * Save Button — Glassmorphic save action button with AccentBlue tint.
 * Ported from LionFitness SaveButton.kt.
 * Used for form save actions in TopBar or standalone.
 */
@Composable
fun CeroFiaoSaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Guardar",
    hazeState: HazeState? = null
) {
    val colors = CeroFiaoTheme.tokens
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f, label = "scale")

    val isBlurEnabled = LocalBlurEnabled.current

    Box(
        modifier = modifier
            .scale(scale)
            .shadow(15.dp, androidx.compose.foundation.shape.RoundedCornerShape(25.dp))
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .then(
                    if (hazeState != null && isBlurEnabled) {
                        Modifier.hazeChild(
                            state = hazeState,
                            style = dev.chrisbanes.haze.HazeStyle(
                                backgroundColor = colors.AccentBlue.copy(alpha = 0.1f),
                                blurRadius = 30.dp,
                                tint = null
                            )
                        )
                    } else Modifier
                )
                .background(colors.AccentBlue.copy(alpha = 0.15f))
                .border(1.dp, colors.AccentBlue.copy(alpha = 0.3f), RoundedCornerShape(25.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 20.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                ),
                color = colors.AccentBlue
            )
        }
    }
}
