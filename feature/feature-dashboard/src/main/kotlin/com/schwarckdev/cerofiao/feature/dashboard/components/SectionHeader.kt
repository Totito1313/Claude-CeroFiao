package com.schwarckdev.cerofiao.feature.dashboard.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

@Composable
fun SectionHeader(
    title: String,
    onViewAll: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val colors = CeroFiaoDesign.colors
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.TextPrimary,
            letterSpacing = (-0.6).sp,
        )
        if (onViewAll != null) {
            Text(
                text = "VER TODO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colors.TextSecondary,
                letterSpacing = 1.2.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .clickable(onClick = onViewAll)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
            )
        }
    }
}
