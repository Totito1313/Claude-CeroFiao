package com.schwarckdev.cerofiao.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.schwarckdev.cerofiao.core.designsystem.theme.BrandGradient

@Composable
fun CeroFiaoFAB(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
                spotColor = Color(0xFF8A2BE2).copy(alpha = 0.5f), // Brand glow
                ambientColor = Color(0xFF8A2BE2).copy(alpha = 0.1f)
            )
            .clip(CircleShape)
            .background(BrandGradient)
            .clickable(onClick = onClick)
            .size(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}
