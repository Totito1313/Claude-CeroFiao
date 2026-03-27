package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes

/**
 * Dropdown picker triggered by a button.
 * Replaces HeroUI's Select component.
 */
@Composable
fun <T> CeroFiaoSelect(
    selected: T?,
    onSelectedChange: (T) -> Unit,
    options: List<T>,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Seleccionar...",
    displayText: (T) -> String = { it.toString() },
    description: String? = null,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    val t = CeroFiaoDesign.colors
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        if (label != null) {
            CeroFiaoLabel(
                text = label,
                modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
            )
        }

        Box {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enabled) { expanded = true },
                shape = RoundedCornerShape(CeroFiaoShapes.SmallCardRadius),
                color = if (enabled) t.SurfaceVariant else t.SurfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(
                    1.dp,
                    if (errorMessage != null) t.Error else t.CardBorder
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selected != null) displayText(selected) else placeholder,
                        color = if (selected != null) t.TextPrimary else t.TextSecondary,
                        fontSize = 15.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Lucide.ChevronDown,
                        contentDescription = null,
                        tint = t.TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = displayText(option),
                                color = if (option == selected) t.Primary else t.TextPrimary
                            )
                        },
                        onClick = {
                            onSelectedChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }

        if (description != null && errorMessage == null) {
            CeroFiaoDescription(
                text = description,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }

        CeroFiaoFieldError(
            message = errorMessage,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
        )
    }
}
