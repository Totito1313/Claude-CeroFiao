package com.schwarckdev.cerofiao.feature.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.User

@Composable
fun DashboardTopBar(
    userName: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = "Bienvenido",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            Text(
                text = userName,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF848487),
            )
        }
        Surface(
            shape = RoundedCornerShape(25.dp),
            color = Color(0xFFF1F1F3).copy(alpha = 0.5f),
            shadowElevation = 8.dp,
        ) {
            Row {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Lucide.Bell,
                        contentDescription = "Notificaciones",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black,
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Lucide.User,
                        contentDescription = "Perfil",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Black,
                    )
                }
            }
        }
    }
}
