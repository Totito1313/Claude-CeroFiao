package com.schwarckdev.cerofiao.feature.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import com.schwarckdev.cerofiao.core.designsystem.theme.BrandGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.components.forms.CeroFiaoTextField
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.ConfigureTopBar
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.TopBarVariant
import com.schwarckdev.cerofiao.core.designsystem.components.navigation.resolveIconKey
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.FeedbackVariant
import com.schwarckdev.cerofiao.core.designsystem.components.utilities.pressableFeedback
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoDesign
import com.schwarckdev.cerofiao.core.model.CategoryType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditCategoryScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditCategoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = CeroFiaoDesign.colors
    val title = if (uiState.isEditMode) "Editar categoría" else "Nueva categoría"

    ConfigureTopBar(variant = TopBarVariant.Detail, title = title, onBackClick = onBack)

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = 70.dp, bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // Preview
        val previewColor = try {
            Color(android.graphics.Color.parseColor(uiState.colorHex))
        } catch (_: Exception) {
            colors.Primary
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(previewColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = resolveIconKey("ic_${uiState.iconName.lowercase()}"),
                        contentDescription = null,
                        tint = previewColor,
                        modifier = Modifier.size(28.dp),
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = uiState.name.ifBlank { "Nombre" },
                    color = if (uiState.name.isBlank()) colors.TextSecondary else colors.TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        // Type toggle
        Column {
            Text("Tipo", color = colors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(CategoryType.EXPENSE to "Gasto", CategoryType.INCOME to "Ingreso").forEach { (type, label) ->
                    val isSelected = uiState.type == type
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .pressableFeedback(
                                onClick = { viewModel.setType(type) },
                                variant = FeedbackVariant.ScaleHighlight,
                            ),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) colors.Primary else colors.CardBackground,
                    ) {
                        Text(
                            text = label,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            color = if (isSelected) colors.TextOnDark else colors.TextSecondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }

        // Name
        CeroFiaoTextField(
            value = uiState.name,
            onValueChange = viewModel::setName,
            label = "Nombre",
            placeholder = "Ej: Comida, Transporte",
            modifier = Modifier.fillMaxWidth(),
        )

        // Color picker
        Column {
            Text("Color", color = colors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))

            val colorOptions = listOf(
                "#FF5722", "#FF9800", "#FFEB3B", "#4CAF50", "#2196F3",
                "#3F51B5", "#9C27B0", "#E91E63", "#F44336", "#00BCD4",
                "#795548", "#607D8B", "#FF9F0A", "#30D158", "#5E5CE6",
                "#BF5AF2", "#FF375F", "#64D2FF",
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                colorOptions.forEach { hex ->
                    val color = try {
                        Color(android.graphics.Color.parseColor(hex))
                    } catch (_: Exception) {
                        colors.Primary
                    }
                    val isSelected = uiState.colorHex.equals(hex, ignoreCase = true)

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .then(
                                if (isSelected) Modifier.border(3.dp, colors.TextPrimary, CircleShape)
                                else Modifier
                            )
                            .pressableFeedback(
                                onClick = { viewModel.setColor(hex) },
                                variant = FeedbackVariant.ScaleHighlight,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isSelected) {
                            Icon(
                                CeroFiaoIcons.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                }
            }
        }

        // Icon picker
        Column {
            Text("Icono", color = colors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))

            val iconOptions = listOf(
                "food", "shopping", "car", "home", "bolt",
                "game", "laptop", "heart", "sim", "work", "box",
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                iconOptions.forEach { iconKey ->
                    val isSelected = uiState.iconName.lowercase() == iconKey
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) previewColor.copy(alpha = 0.2f) else colors.CardBackground
                            )
                            .then(
                                if (isSelected) Modifier.border(2.dp, previewColor, RoundedCornerShape(12.dp))
                                else Modifier
                            )
                            .pressableFeedback(
                                onClick = { viewModel.setIcon(iconKey) },
                                variant = FeedbackVariant.ScaleHighlight,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = resolveIconKey("ic_$iconKey"),
                            contentDescription = iconKey,
                            tint = if (isSelected) previewColor else colors.TextSecondary,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                }
            }
        }

        // Parent category (optional)
        if (uiState.parentCategories.isNotEmpty()) {
            Column {
                Text("Categoría padre (opcional)", color = colors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))

                // "None" option
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (uiState.parentId == null) Modifier.border(1.5.dp, colors.Primary, RoundedCornerShape(12.dp))
                            else Modifier
                        )
                        .pressableFeedback(
                            onClick = { viewModel.setParent(null) },
                            variant = FeedbackVariant.ScaleHighlight,
                        ),
                    shape = RoundedCornerShape(12.dp),
                    color = if (uiState.parentId == null) colors.Primary.copy(alpha = 0.1f) else colors.CardBackground,
                ) {
                    Text(
                        text = "Sin padre (categoría raíz)",
                        modifier = Modifier.padding(14.dp),
                        color = if (uiState.parentId == null) colors.Primary else colors.TextSecondary,
                        fontSize = 14.sp,
                    )
                }

                Spacer(Modifier.height(6.dp))

                uiState.parentCategories.forEach { parent ->
                    val isSelected = uiState.parentId == parent.id
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .then(
                                if (isSelected) Modifier.border(1.5.dp, colors.Primary, RoundedCornerShape(12.dp))
                                else Modifier
                            )
                            .pressableFeedback(
                                onClick = { viewModel.setParent(parent.id) },
                                variant = FeedbackVariant.ScaleHighlight,
                            ),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) colors.Primary.copy(alpha = 0.1f) else colors.CardBackground,
                    ) {
                        Text(
                            text = parent.name,
                            modifier = Modifier.padding(14.dp),
                            color = if (isSelected) colors.Primary else colors.TextPrimary,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
        }

        // Save button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (uiState.name.isNotBlank()) BrandGradient
                    else Brush.horizontalGradient(listOf(colors.SurfaceVariant, colors.SurfaceVariant)),
                    RoundedCornerShape(16.dp),
                )
                .then(
                    if (uiState.name.isNotBlank()) Modifier.pressableFeedback(
                        onClick = { viewModel.save() },
                        variant = FeedbackVariant.ScaleHighlight,
                    ) else Modifier
                )
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = if (uiState.isSaving) "Guardando..." else "Guardar",
                color = if (uiState.name.isNotBlank()) colors.TextOnDark else colors.TextSecondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
