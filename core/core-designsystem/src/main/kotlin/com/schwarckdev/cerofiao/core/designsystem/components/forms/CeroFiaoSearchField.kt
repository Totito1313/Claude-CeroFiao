package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.X
import com.schwarckdev.cerofiao.core.designsystem.components.feedback.CeroFiaoSpinner
import com.schwarckdev.cerofiao.core.designsystem.components.feedback.SpinnerSize
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Search input with pill shape, search icon, and clear button.
 * HeroUI SearchInput V3: radius=full, flat variant, keyboard=search.
 *
 * Sizes:
 *  - Sm → height 32dp, font 12sp, icon 14dp
 *  - Md → height 40dp, font 14sp, icon 16dp  [default]
 *  - Lg → height 48dp, font 16sp, icon 18dp
 */
@Composable
fun CeroFiaoSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Buscar...",
    size: InputSize = InputSize.Md,
    onClear: (() -> Unit)? = null,
    filterIcon: (@Composable () -> Unit)? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Search,
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val t = CeroFiaoDesign.colors
    var isFocused by remember { mutableStateOf(false) }

    val height: Dp = when (size) { InputSize.Sm -> 32.dp; InputSize.Md -> 40.dp; InputSize.Lg -> 48.dp }
    val fontSize = when (size) { InputSize.Sm -> 12.sp; InputSize.Md -> 14.sp; InputSize.Lg -> 16.sp }
    val iconSize: Dp = when (size) { InputSize.Sm -> 14.dp; InputSize.Md -> 16.dp; InputSize.Lg -> 18.dp }
    val hPad: Dp = when (size) { InputSize.Sm -> 10.dp; InputSize.Md -> 12.dp; InputSize.Lg -> 14.dp }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = t.SurfaceVariant,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(horizontal = hPad),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Lucide.Search,
                contentDescription = null,
                tint = if (isFocused) t.Primary else t.TextSecondary,
                modifier = Modifier.size(iconSize),
            )
            Spacer(Modifier.width(8.dp))

            Box(modifier = Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = t.TextSecondary,
                        fontSize = fontSize,
                    )
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isFocused = it.isFocused },
                    singleLine = true,
                    enabled = enabled,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    textStyle = TextStyle(color = t.TextPrimary, fontSize = fontSize),
                    cursorBrush = SolidColor(t.Primary),
                )
            }

            Spacer(Modifier.width(4.dp))
            when {
                isLoading -> {
                    CeroFiaoSpinner(size = SpinnerSize.Small)
                }
                query.isNotEmpty() -> {
                    IconButton(
                        onClick = { if (onClear != null) onClear() else onQueryChange("") },
                        modifier = Modifier.size(iconSize + 8.dp),
                    ) {
                        Icon(
                            imageVector = Lucide.X,
                            contentDescription = "Limpiar",
                            tint = t.TextSecondary,
                            modifier = Modifier.size(iconSize - 2.dp),
                        )
                    }
                }
                filterIcon != null -> filterIcon()
            }
        }
    }
}
