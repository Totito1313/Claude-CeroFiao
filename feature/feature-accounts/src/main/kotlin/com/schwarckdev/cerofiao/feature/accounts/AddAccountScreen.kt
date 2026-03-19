package com.schwarckdev.cerofiao.feature.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import com.schwarckdev.cerofiao.core.designsystem.icon.CeroFiaoIcons
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.designsystem.theme.CeroFiaoTheme
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.AccountPlatform
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
) : ViewModel() {

    fun createAccount(
        name: String,
        platform: AccountPlatform,
        currencyCode: String,
        initialBalance: Double,
        onSuccess: () -> Unit,
    ) {
        viewModelScope.launch {
            val now = DateUtils.now()
            val account = Account(
                id = UuidGenerator.generate(),
                name = name,
                type = platform.defaultType,
                platform = platform,
                currencyCode = currencyCode,
                balance = initialBalance,
                initialBalance = initialBalance,
                isActive = true,
                includeInTotal = true,
                createdAt = now,
                updatedAt = now,
            )
            accountRepository.createAccount(account)
            onSuccess()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(
    onBack: () -> Unit,
    onAccountCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddAccountViewModel = hiltViewModel(),
) {
    val t = CeroFiaoTheme.tokens
    var name by remember { mutableStateOf("") }
    var selectedPlatform by remember { mutableStateOf(AccountPlatform.NONE) }
    var currencyCode by remember { mutableStateOf(AccountPlatform.NONE.defaultCurrencyCode) }
    var initialBalanceText by remember { mutableStateOf("") }
    var platformDropdownExpanded by remember { mutableStateOf(false) }
    var currencyDropdownExpanded by remember { mutableStateOf(false) }
    val supportedCurrencies = listOf("USD", "VES", "USDT", "EUR", "EURI")

    val isFormValid = name.isNotBlank()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(t.bg),
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
                        imageVector = CeroFiaoIcons.Back,
                        contentDescription = "Volver",
                        tint = t.text,
                    )
                }
            }
            Text(
                text = "Nueva Cuenta",
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Nombre de la cuenta") },
                placeholder = { Text(text = "Ej: Banesco Corriente") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            ExposedDropdownMenuBox(
                expanded = platformDropdownExpanded,
                onExpandedChange = { platformDropdownExpanded = it },
            ) {
                OutlinedTextField(
                    value = selectedPlatform.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(text = "Plataforma") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = platformDropdownExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                )

                ExposedDropdownMenu(
                    expanded = platformDropdownExpanded,
                    onDismissRequest = { platformDropdownExpanded = false },
                ) {
                    AccountPlatform.entries.forEach { platform ->
                        DropdownMenuItem(
                            text = { Text(text = platform.displayName) },
                            onClick = {
                                selectedPlatform = platform
                                currencyCode = platform.defaultCurrencyCode
                                platformDropdownExpanded = false
                            },
                        )
                    }
                }
            }

            OutlinedTextField(
                value = selectedPlatform.defaultType.name,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "Tipo") },
                modifier = Modifier.fillMaxWidth(),
            )

            ExposedDropdownMenuBox(
                expanded = currencyDropdownExpanded,
                onExpandedChange = { currencyDropdownExpanded = it },
            ) {
                OutlinedTextField(
                    value = currencyCode,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(text = "Moneda") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyDropdownExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                )

                ExposedDropdownMenu(
                    expanded = currencyDropdownExpanded,
                    onDismissRequest = { currencyDropdownExpanded = false },
                ) {
                    supportedCurrencies.forEach { code ->
                        DropdownMenuItem(
                            text = { Text(text = code) },
                            onClick = {
                                currencyCode = code
                                currencyDropdownExpanded = false
                            },
                        )
                    }
                }
            }

            OutlinedTextField(
                value = initialBalanceText,
                onValueChange = { value ->
                    if (value.isEmpty() || value.matches(Regex("^\\d*\\.?\\d*$"))) {
                        initialBalanceText = value
                    }
                },
                label = { Text(text = "Balance inicial") },
                placeholder = { Text(text = "0.00") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val balance = initialBalanceText.toDoubleOrNull() ?: 0.0
                    viewModel.createAccount(
                        name = name.trim(),
                        platform = selectedPlatform,
                        currencyCode = currencyCode.trim().uppercase(),
                        initialBalance = balance,
                        onSuccess = onAccountCreated,
                    )
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            ) {
                Text(
                    text = "Crear Cuenta",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}
