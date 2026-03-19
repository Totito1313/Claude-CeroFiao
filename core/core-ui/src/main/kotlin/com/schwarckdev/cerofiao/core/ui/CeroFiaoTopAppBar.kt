package com.schwarckdev.cerofiao.core.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme

@Composable
fun CeroFiaoTopAppBar(
    title: String,
    onNavigationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val t = CeroFiaoTheme.tokens

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .windowInsetsPadding(WindowInsets.statusBars),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(shape = CircleShape, color = t.iconBg) {
            IconButton(onClick = onNavigationClick) {
                Icon(CeroFiaoIcons.Back, contentDescription = "Volver", tint = t.text)
            }
        }
        Text(
            text = title,
            color = t.text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}
