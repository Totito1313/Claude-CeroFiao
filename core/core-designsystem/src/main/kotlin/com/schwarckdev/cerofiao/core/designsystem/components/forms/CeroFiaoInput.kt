package com.schwarckdev.cerofiao.core.designsystem.components.forms

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign

/** HeroUI Input display variant */
enum class InputDisplayVariant {
    Flat,       // SurfaceVariant bg, no border (default)
    Faded,      // SurfaceVariant bg + subtle border
    Bordered,   // Transparent bg + colored border on focus
    Underlined  // No bg, bottom line only
}

/** Focus ring / accent color */
enum class InputColor { Default, Primary, Success, Warning, Danger }

/** Input field size */
enum class InputSize { Sm, Md, Lg }

/** Where the label is placed relative to the field */
enum class InputLabelPlacement { Inside, Outside }

/**
 * Low-level styled text input with HeroUI V3 variants, sizes, colors, and animated inside label.
 * Flat/Faded/Bordered/Underlined × Default/Primary/Success/Warning/Danger × Sm/Md/Lg.
 */
@Composable
fun CeroFiaoInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    labelPlacement: InputLabelPlacement = InputLabelPlacement.Outside,
    placeholder: String? = null,
    displayVariant: InputDisplayVariant = InputDisplayVariant.Flat,
    color: InputColor = InputColor.Default,
    size: InputSize = InputSize.Md,
    enabled: Boolean = true,
    isError: Boolean = false,
    isClearable: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val t = CeroFiaoDesign.colors
    var isFocused by remember { mutableStateOf(false) }
    val isFocusedOrFilled = isFocused || value.isNotEmpty()

    // ── Size tokens ──────────────────────────────────────────────────
    val baseHeight: Dp = when (size) { InputSize.Sm -> 32.dp; InputSize.Md -> 40.dp; InputSize.Lg -> 48.dp }
    val insideHeight: Dp = when (size) { InputSize.Sm -> 48.dp; InputSize.Md -> 56.dp; InputSize.Lg -> 64.dp }
    // HeroUI V3: Sm=12, Md=12, Lg=14
    val hPadding: Dp = when (size) { InputSize.Sm -> 12.dp; InputSize.Md -> 12.dp; InputSize.Lg -> 14.dp }
    val valueFontSize = when (size) { InputSize.Sm -> 12.sp; InputSize.Md -> 14.sp; InputSize.Lg -> 16.sp }
    // HeroUI V3: Sm=8, Md=12, Lg=12
    val radius: Dp = when (size) { InputSize.Sm -> 8.dp; InputSize.Md -> 12.dp; InputSize.Lg -> 12.dp }
    val iconSize: Dp = when (size) { InputSize.Sm -> 14.dp; InputSize.Md -> 16.dp; InputSize.Lg -> 18.dp }

    val hasInsideLabel = label != null && labelPlacement == InputLabelPlacement.Inside
    val containerHeight = if (hasInsideLabel) insideHeight else baseHeight

    // ── Focus / error colors ─────────────────────────────────────────
    val focusColor: Color = if (isError) t.Error else when (color) {
        InputColor.Default -> t.TextPrimary
        InputColor.Primary -> t.Primary
        InputColor.Success -> t.Success
        InputColor.Warning -> t.Warning
        InputColor.Danger -> t.Error
    }

    // ── Variant backgrounds & borders ────────────────────────────────
    val bgColor: Color = when (displayVariant) {
        InputDisplayVariant.Flat, InputDisplayVariant.Faded -> t.SurfaceVariant
        InputDisplayVariant.Bordered, InputDisplayVariant.Underlined -> Color.Transparent
    }
    val borderColor: Color = when {
        isError -> t.Error
        isFocused -> focusColor
        else -> t.CardBorder
    }
    val borderWidth: Dp = if (isFocused || isError) 2.dp else 1.dp
    val border: BorderStroke? = when (displayVariant) {
        InputDisplayVariant.Faded -> BorderStroke(borderWidth, borderColor)
        InputDisplayVariant.Bordered -> BorderStroke(borderWidth, borderColor)
        else -> null
    }
    val underlineMod = if (displayVariant == InputDisplayVariant.Underlined) {
        Modifier.drawBehind {
            drawBottomLine(borderColor, borderWidth.toPx())
        }
    } else Modifier

    val shape = if (displayVariant == InputDisplayVariant.Underlined) RoundedCornerShape(0.dp)
    else RoundedCornerShape(radius)

    // ── Inside label animation ────────────────────────────────────────
    // Collapsed offsets (label at top): Sm=6, Md=8, Lg=10
    val collapsedY: Dp = when (size) { InputSize.Sm -> 6.dp; InputSize.Md -> 8.dp; InputSize.Lg -> 10.dp }
    // Expanded offsets (label centered): Sm=15, Md=20, Lg=24
    val expandedY: Dp = when (size) { InputSize.Sm -> 15.dp; InputSize.Md -> 20.dp; InputSize.Lg -> 24.dp }

    val labelOffsetY by animateDpAsState(
        targetValue = if (isFocusedOrFilled) collapsedY else expandedY,
        animationSpec = tween(150), label = "labelY"
    )
    val labelFontSizeAnim by animateFloatAsState(
        targetValue = if (isFocusedOrFilled) when (size) {
            InputSize.Sm -> 10f; InputSize.Md -> 12f; InputSize.Lg -> 13f
        } else valueFontSize.value,
        animationSpec = tween(150), label = "labelSize"
    )
    val labelColor: Color = when {
        isError -> t.Error
        isFocused && color != InputColor.Default -> focusColor
        else -> t.TextSecondary
    }

    Surface(
        modifier = modifier.then(underlineMod),
        shape = shape,
        color = if (enabled) bgColor else bgColor.copy(alpha = 0.5f),
        border = border,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(containerHeight)
                .padding(horizontal = hPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) {
                Box(Modifier.size(iconSize), contentAlignment = Alignment.Center) { leadingIcon() }
                Spacer(Modifier.width(8.dp))
            }

            Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                if (hasInsideLabel) {
                    // Animated floating label
                    Text(
                        text = label!!,
                        fontSize = labelFontSizeAnim.sp,
                        color = labelColor,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(y = labelOffsetY),
                    )
                    // Single BasicTextField anchored at bottom
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(bottom = 8.dp)
                            .onFocusChanged { isFocused = it.isFocused },
                        singleLine = singleLine,
                        minLines = minLines,
                        maxLines = maxLines,
                        enabled = enabled,
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                        textStyle = TextStyle(
                            // Hide text while label is acting as placeholder
                            color = if (isFocusedOrFilled) t.TextPrimary else Color.Transparent,
                            fontSize = valueFontSize,
                        ),
                        cursorBrush = SolidColor(focusColor),
                    )
                } else {
                    // Outside label or no label — standard input with placeholder
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            color = t.TextSecondary,
                            fontSize = valueFontSize,
                            modifier = Modifier.align(Alignment.CenterStart),
                        )
                    }
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterStart)
                            .onFocusChanged { isFocused = it.isFocused },
                        singleLine = singleLine,
                        minLines = minLines,
                        maxLines = maxLines,
                        enabled = enabled,
                        keyboardOptions = keyboardOptions,
                        keyboardActions = keyboardActions,
                        textStyle = TextStyle(
                            color = if (enabled) t.TextPrimary else t.TextSecondary,
                            fontSize = valueFontSize,
                        ),
                        cursorBrush = SolidColor(focusColor),
                    )
                }
            }

            // Clear button takes priority over trailingIcon when isClearable and value is non-empty
            val showClear = isClearable && value.isNotEmpty() && enabled
            when {
                showClear -> {
                    Spacer(Modifier.width(4.dp))
                    IconButton(
                        onClick = { onValueChange("") },
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
                trailingIcon != null -> {
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.size(iconSize), contentAlignment = Alignment.Center) { trailingIcon() }
                }
            }
        }
    }
}

private fun DrawScope.drawBottomLine(color: Color, strokeWidth: Float) {
    drawLine(
        color = color,
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height),
        strokeWidth = strokeWidth,
    )
}
