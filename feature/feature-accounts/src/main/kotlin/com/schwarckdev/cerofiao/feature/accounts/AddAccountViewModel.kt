package com.schwarckdev.cerofiao.feature.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.AccountPlatform
import com.schwarckdev.cerofiao.core.model.AccountType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddAccountUiState(
    val name: String = "",
    val type: AccountType = AccountType.BANK,
    val platform: AccountPlatform = AccountPlatform.NONE,
    val currencyCode: String = "USD",
    val initialBalance: String = "",
    val includeInTotal: Boolean = true,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
)

@HiltViewModel
class AddAccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddAccountUiState())
    val uiState: StateFlow<AddAccountUiState> = _uiState.asStateFlow()

    fun setName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun setType(type: AccountType) {
        _uiState.update { it.copy(type = type) }
    }

    fun setPlatform(platform: AccountPlatform) {
        _uiState.update {
            it.copy(
                platform = platform,
                name = if (it.name.isBlank() || AccountPlatform.entries.any { p -> p.displayName == it.name }) {
                    platform.displayName
                } else it.name,
                type = platform.defaultType,
                currencyCode = platform.defaultCurrencyCode,
            )
        }
    }

    fun setCurrency(code: String) {
        _uiState.update { it.copy(currencyCode = code) }
    }

    fun setInitialBalance(value: String) {
        _uiState.update { it.copy(initialBalance = value) }
    }

    fun setIncludeInTotal(include: Boolean) {
        _uiState.update { it.copy(includeInTotal = include) }
    }

    fun save() {
        val state = _uiState.value
        if (state.name.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val balance = state.initialBalance.toDoubleOrNull() ?: 0.0
            accountRepository.createAccount(
                Account(
                    id = UuidGenerator.generate(),
                    name = state.name.trim(),
                    type = state.type,
                    platform = state.platform,
                    currencyCode = state.currencyCode,
                    balance = balance,
                    initialBalance = balance,
                    includeInTotal = state.includeInTotal,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                ),
            )
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
