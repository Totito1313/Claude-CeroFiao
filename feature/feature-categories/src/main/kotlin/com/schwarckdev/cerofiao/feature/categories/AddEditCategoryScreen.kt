package com.schwarckdev.cerofiao.feature.categories
import com.schwarckdev.cerofiao.core.ui.navigation.TopBarVariant

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import com.schwarckdev.cerofiao.core.ui.CeroFiaoButton
import com.schwarckdev.cerofiao.core.ui.CeroFiaoTextField
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoShapes
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.model.CategoryType

private val colorPalette = listOf(
    "#FF5722", "#F44336", "#E91E63", "#9C27B0",
    "#673AB7", "#3F51B5", "#2196F3", "#03A9F4",
    "#00BCD4", "#009688", "#4CAF50", "#8BC34A",
    "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800",
    "#795548", "#607D8B", "#9E9E9E", "#000000",
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditCategoryScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditCategoryViewModel = hiltViewModel(),
) {
    val t = CeroFiaoTheme.tokens
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg).padding(top = 90.dp),
    ) {
        // Top bar row with back button and title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = CircleShape,
                color = t.iconBg,
                modifier = Modifier.size(40.dp),
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        CeroFiaoIcons.Back,
                        contentDescription = "Volver",
                        tint = t.text,
                    )
                }
            }
            Text(
                text = if (uiState.isEditMode) "Editar categoría" else "Nueva categoría",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = t.text,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Name
            CeroFiaoTextField(
                value = uiState.name,
                onValueChange = viewModel::setName,
                label = "Nombre",
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            // Type selector
            Text(text = "Tipo", style = MaterialTheme.typography.labelLarge, color = t.text)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val types = listOf(CategoryType.EXPENSE to "Gasto", CategoryType.INCOME to "Ingreso")
                types.forEach { (type, label) ->
                    OptionChip(
                        label = label,
                        selected = uiState.type == type,
                        onClick = { viewModel.setType(type) }
                    )
                }
            }

            // Parent category selector
            if (uiState.parentCategories.isNotEmpty()) {
                Text(text = "Categoría Padre (Opcional)", style = MaterialTheme.typography.labelLarge, color = t.text)
                var expanded by remember { mutableStateOf(false) }
                val selectedParent = uiState.parentCategories.find { it.id == uiState.parentId }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                ) {
                    CeroFiaoTextField(
                        value = selectedParent?.name ?: "Ninguna (Raíz)",
                        onValueChange = {},
                        label = "Categoría Padre",
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("Ninguna (Raíz)") },
                            onClick = {
                                viewModel.setParent(null)
                                expanded = false
                            },
                        )
                        uiState.parentCategories.forEach { parent ->
                            DropdownMenuItem(
                                text = { Text(parent.name) },
                                onClick = {
                                    viewModel.setParent(parent.id)
                                    expanded = false
                                },
                            )
                        }
                    }
                }
            }

            // Color picker
            Text(text = "Color", style = MaterialTheme.typography.labelLarge, color = t.text)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                colorPalette.forEach { hex ->
                    val color = try {
                        Color(android.graphics.Color.parseColor(hex))
                    } catch (_: Exception) {
                        Color.Gray
                    }
                    val isSelected = uiState.colorHex.equals(hex, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(color)
                            .then(
                                if (isSelected) {
                                    Modifier.border(3.dp, t.text, CircleShape)
                                } else {
                                    Modifier
                                },
                            )
                            .clickable { viewModel.setColor(hex) },
                    )
                }
            }

            // Icon picker
            Text(text = "Icono", style = MaterialTheme.typography.labelLarge, color = t.text)

            val selectedColor = try {
                Color(android.graphics.Color.parseColor(uiState.colorHex))
            } catch (_: Exception) {
                Color(0xFF8A2BE2)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(CeroFiaoIcons.allCategoryIcons) { (name, resId) ->
                    val isSelected = uiState.iconName == name
                    Surface(
                        modifier = Modifier
                            .size(56.dp)
                            .clickable { viewModel.setIcon(name) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) {
                            selectedColor.copy(alpha = 0.15f)
                        } else {
                            t.pillBg
                        },
                        tonalElevation = if (isSelected) 2.dp else 0.dp,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                painter = painterResource(resId),
                                contentDescription = name,
                                tint = if (isSelected) selectedColor else t.textSecondary,
                                modifier = Modifier.size(28.dp),
                            )
                        }
                    }
                }
            }

            // Save button
            CeroFiaoButton(
                text = "Guardar",
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving && uiState.name.isNotBlank(),
            )
        }
    }
}

@Composable
private fun OptionChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val t = CeroFiaoTheme.tokens
    val bgColor = if (selected) Color(0x148A2BE2) else t.pillBg
    val borderColor = if (selected) Color(0x268A2BE2) else Color.Transparent
    val textColor = if (selected) Color(0xFF8A2BE2) else t.textSecondary

    Surface(
        modifier = modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(CeroFiaoShapes.ChipRadius))
            .clickable(onClick = onClick),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(CeroFiaoShapes.ChipRadius),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
    ) {
        androidx.compose.material3.Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

