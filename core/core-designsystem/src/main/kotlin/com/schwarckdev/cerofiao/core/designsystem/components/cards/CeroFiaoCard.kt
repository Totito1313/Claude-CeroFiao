package com.schwarckdev.cerofiao.core.designsystem.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.FeedbackVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.pressableFeedback
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import com.schwarckdev.cerofiao.core.designsystem.theme.LocalCardConfig

enum class CardVariant {
    Default,
    Secondary,
    Tertiary,
    Transparent
}

/**
 * Card container with flexible layout sections.
 * Replaces HeroUI's Card component. Unifies CeroFiaoGlassCard + GlassCard.
 *
 * Compound API: use CeroFiaoCardHeader, CeroFiaoCardBody, CeroFiaoCardFooter,
 * CeroFiaoCardTitle, CeroFiaoCardDescription inside the content lambda.
 */
@Composable
fun CeroFiaoCard(
    modifier: Modifier = Modifier,
    variant: CardVariant = CardVariant.Default,
    onClick: (() -> Unit)? = null,
    feedback: FeedbackVariant = FeedbackVariant.ScaleHighlight,
    content: @Composable ColumnScope.() -> Unit
) {
    val t = CeroFiaoDesign.colors
    val cardConfig = LocalCardConfig.current

    val backgroundColor = when (variant) {
        CardVariant.Default -> t.CardBackground.copy(alpha = cardConfig.backgroundOpacity)
        CardVariant.Secondary -> t.SurfaceVariant
        CardVariant.Tertiary -> t.Surface
        CardVariant.Transparent -> Color.Transparent
    }

    val borderColor = when (variant) {
        CardVariant.Default -> t.CardBorder.copy(alpha = cardConfig.borderOpacity)
        CardVariant.Secondary -> t.CardBorder
        CardVariant.Tertiary -> t.CardBorder.copy(alpha = 0.5f)
        CardVariant.Transparent -> Color.Transparent
    }

    val shape = RoundedCornerShape(cardConfig.cornerRadius)

    val clickModifier = if (onClick != null) {
        Modifier.pressableFeedback(onClick = onClick, variant = feedback)
    } else Modifier

    Surface(
        modifier = modifier
            .then(clickModifier)
            .clip(shape),
        shape = shape,
        color = backgroundColor,
        border = if (variant != CardVariant.Transparent) {
            BorderStroke(cardConfig.borderWidth, borderColor)
        } else null
    ) {
        Column(content = content)
    }
}


@Composable
fun CeroFiaoCardHeader(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        content()
    }
}

@Composable
fun CeroFiaoCardBody(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        content()
    }
}

@Composable
fun CeroFiaoCardFooter(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.End,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = horizontalArrangement
    ) {
        content()
    }
}

@Composable
fun CeroFiaoCardTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        color = CeroFiaoDesign.colors.TextPrimary,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun CeroFiaoCardDescription(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        color = CeroFiaoDesign.colors.TextSecondary,
        fontSize = 13.sp,
        lineHeight = 18.sp
    )
}
