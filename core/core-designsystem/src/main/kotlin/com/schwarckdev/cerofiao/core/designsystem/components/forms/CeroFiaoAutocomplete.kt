package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import com.schwarckdev.cerofiao.core.designsystem.components.feedback.CeroFiaoSpinner
import com.schwarckdev.cerofiao.core.designsystem.components.feedback.SpinnerSize
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes

data class AutocompleteItem<T>(
    val key: String,
    val label: String,
    val value: T,
    val description: String? = null,
    val startContent: (@Composable () -> Unit)? = null,
)

/**
 * Searchable dropdown that filters items as the user types.
 * HeroUI Autocomplete V3: editable input + suggestion list below.
 */
@Composable
fun <T> CeroFiaoAutocomplete(
    items: List<AutocompleteItem<T>>,
    selectedKey: String?,
    onSelectionChange: (AutocompleteItem<T>) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Buscar...",
    description: String? = null,
    errorMessage: String? = null,
    isRequired: Boolean = false,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    displayVariant: InputDisplayVariant = InputDisplayVariant.Flat,
    color: InputColor = InputColor.Default,
    size: InputSize = InputSize.Md,
) {
    val t = CeroFiaoDesign.colors
    var query by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val isOpen = isFocused && !isLoading

    // Update query when selection changes externally
    val selectedItem = items.firstOrNull { it.key == selectedKey }
    val displayQuery = if (isFocused) query else selectedItem?.label ?: query

    val filtered = remember(query, items) {
        if (query.isBlank()) items
        else items.filter { it.label.contains(query, ignoreCase = true) }
    }

    val chevronRotation by animateFloatAsState(
        targetValue = if (isOpen) 180f else 0f,
        label = "chevronRotation"
    )

    Column(modifier = modifier) {
        if (label != null) {
            CeroFiaoLabel(
                text = label,
                isRequired = isRequired,
                color = when (color) {
                    InputColor.Default -> LabelColor.Default
                    InputColor.Primary -> LabelColor.Primary
                    InputColor.Success -> LabelColor.Success
                    InputColor.Warning -> LabelColor.Warning
                    InputColor.Danger -> LabelColor.Danger
                },
                size = when (size) {
                    InputSize.Sm -> LabelSize.Sm
                    InputSize.Md -> LabelSize.Md
                    InputSize.Lg -> LabelSize.Lg
                },
                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp),
            )
        }

        Box {
            CeroFiaoInput(
                value = displayQuery,
                onValueChange = { newQuery ->
                    query = newQuery
                    // Clear selection if user is typing something different
                    if (selectedItem != null && newQuery != selectedItem.label) {
                        // keep internal query, selection will be cleared by parent via onSelectionChange
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        isFocused = it.isFocused
                        if (it.isFocused) query = ""
                        else if (selectedItem != null) query = selectedItem.label
                        else query = ""
                    },
                placeholder = placeholder,
                displayVariant = displayVariant,
                color = color,
                size = size,
                enabled = enabled,
                isError = errorMessage != null,
                trailingIcon = {
                    if (isLoading) {
                        CeroFiaoSpinner(size = SpinnerSize.Small)
                    } else {
                        Icon(
                            imageVector = Lucide.ChevronDown,
                            contentDescription = null,
                            tint = t.TextSecondary,
                            modifier = Modifier
                                .size(16.dp)
                                .rotate(chevronRotation),
                        )
                    }
                },
            )

            // Dropdown popup
            if (isOpen) {
                Popup(
                    onDismissRequest = { isFocused = false },
                    properties = PopupProperties(focusable = false),
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 240.dp),
                        shape = RoundedCornerShape(CeroFiaoShapes.SmallCardRadius),
                        color = t.Surface,
                        shadowElevation = 8.dp,
                    ) {
                        Column {
                            if (filtered.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = "Sin resultados",
                                        color = t.TextSecondary,
                                        fontSize = 14.sp,
                                    )
                                }
                            } else {
                                filtered.forEachIndexed { index, item ->
                                    AutocompleteItemRow(
                                        item = item,
                                        isSelected = item.key == selectedKey,
                                        onClick = {
                                            onSelectionChange(item)
                                            query = item.label
                                            isFocused = false
                                        },
                                    )
                                    if (index < filtered.lastIndex) {
                                        HorizontalDivider(
                                            color = t.CardBorder,
                                            thickness = 0.5.dp,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (description != null && errorMessage == null) {
            CeroFiaoDescription(
                text = description,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp),
            )
        }
        CeroFiaoFieldError(
            message = errorMessage,
            modifier = Modifier.padding(top = 4.dp, start = 4.dp),
        )
    }
}

@Composable
private fun <T> AutocompleteItemRow(
    item: AutocompleteItem<T>,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val t = CeroFiaoDesign.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (item.startContent != null) {
            item.startContent.invoke()
            Spacer(Modifier.width(10.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.label,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) t.Primary else t.TextPrimary,
            )
            if (item.description != null) {
                Text(
                    text = item.description,
                    fontSize = 12.sp,
                    color = t.TextSecondary,
                )
            }
        }

        if (isSelected) {
            Icon(
                imageVector = Lucide.Check,
                contentDescription = null,
                tint = t.Primary,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}
