package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Search
import com.composables.icons.lucide.X
import com.schwarckdev.cerofiao.core.designsystem.components.feedback.CeroFiaoSpinner
import com.schwarckdev.cerofiao.core.designsystem.components.feedback.SpinnerSize
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/**
 * Search input with icon, clear button, and optional filter.
 * Replaces HeroUI's SearchField component.
 */
@Composable
fun CeroFiaoSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Buscar...",
    onClear: (() -> Unit)? = null,
    filterIcon: (@Composable () -> Unit)? = null,
    isLoading: Boolean = false
) {
    val t = CeroFiaoDesign.colors

    CeroFiaoInput(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = placeholder,
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Lucide.Search,
                contentDescription = null,
                tint = t.TextSecondary,
                modifier = Modifier.size(18.dp)
            )
        },
        trailingIcon = {
            when {
                isLoading -> CeroFiaoSpinner(size = SpinnerSize.Small)
                query.isNotEmpty() -> {
                    IconButton(
                        onClick = {
                            if (onClear != null) onClear()
                            else onQueryChange("")
                        },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Lucide.X,
                            contentDescription = "Limpiar",
                            tint = t.TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                filterIcon != null -> filterIcon()
            }
        }
    )
}
