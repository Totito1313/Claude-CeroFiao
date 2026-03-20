package dev.chrisbanes.haze

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class HazeState

class HazeTint(val color: Color)

class HazeStyle(
    val backgroundColor: Color = Color.Transparent, 
    val blurRadius: Dp = 0.dp, 
    val tint: HazeTint? = null
)

fun Modifier.hazeChild(state: HazeState?, style: HazeStyle? = null): Modifier = this
