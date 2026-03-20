package com.schwarckdev.cerofiao.feature.settings
import com.schwarckdev.cerofiao.core.ui.navigation.TopBarVariant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.ui.EmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssociatedTitlesScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AssociatedTitlesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val t = CeroFiaoTheme.tokens

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg).padding(top = 90.dp),
    ) {
        // CeroFiao top bar
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
                    Icon(CeroFiaoIcons.Back, contentDescription = "Volver", tint = t.text)
                }
            }
            Text(
                text = "Títulos asociados",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = t.text,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        if (uiState.titles.isEmpty()) {
            EmptyState(
                icon = CeroFiaoIcons.Transactions,
                title = "Sin asociaciones",
                description = "Las asociaciones se crean automáticamente al guardar transacciones con nota y categoría",
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            ) {
                items(uiState.titles, key = { it.title.id }) { item ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = t.surface,
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = t.text,
                                )
                                Text(
                                    text = item.categoryName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = t.textSecondary,
                                )
                            }
                            IconButton(onClick = { viewModel.delete(item.title.id) }) {
                                Icon(
                                    CeroFiaoIcons.Delete,
                                    contentDescription = "Eliminar",
                                    tint = t.danger,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

