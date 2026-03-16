package com.schwarckdev.cerofiao.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.domain.usecase.GetAccountsUseCase
import com.schwarckdev.cerofiao.core.domain.usecase.RecordTransferUseCase
import com.schwarckdev.cerofiao.core.model.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransferUiState(
    val accounts: List<Account> = emptyList(),
    val fromAccountId: String? = null,
    val toAccountId: String? = null,
    val amount: String = "",
    val receivedAmount: String = "",
    val commissionPercent: String = "",
    val note: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isCrossCurrency: Boolean = false,
) {
    val fromAccount: Account? get() = accounts.find { it.id == fromAccountId }
    val toAccount: Account? get() = accounts.find { it.id == toAccountId }
    val isValid: Boolean
        get() = fromAccountId != null &&
            toAccountId != null &&
            fromAccountId != toAccountId &&
            (amount.toDoubleOrNull() ?: 0.0) > 0
}

@HiltViewModel
class TransferViewModel @Inject constructor(
    getAccountsUseCase: GetAccountsUseCase,
    private val recordTransferUseCase: RecordTransferUseCase,
) : ViewModel() {

    private val form = MutableStateFlow(TransferForm())

    val uiState: StateFlow<TransferUiState> = combine(
        getAccountsUseCase(),
        form,
    ) { accounts, formData ->
        val fromAccount = accounts.find { it.id == formData.fromAccountId }
        val toAccount = accounts.find { it.id == formData.toAccountId }
        val isCrossCurrency = fromAccount != null && toAccount != null &&
            fromAccount.currencyCode != toAccount.currencyCode

        TransferUiState(
            accounts = accounts,
            fromAccountId = formData.fromAccountId,
            toAccountId = formData.toAccountId,
            amount = formData.amount,
            receivedAmount = formData.receivedAmount,
            commissionPercent = formData.commissionPercent,
            note = formData.note,
            isSaving = formData.isSaving,
            isSaved = formData.isSaved,
            isCrossCurrency = isCrossCurrency,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TransferUiState(),
    )

    fun selectFromAccount(accountId: String) {
        form.update { it.copy(fromAccountId = accountId) }
    }

    fun selectToAccount(accountId: String) {
        form.update { it.copy(toAccountId = accountId) }
    }

    fun setAmount(value: String) {
        if (value.isEmpty() || value.matches(Regex("^\\d*\\.?\\d*$"))) {
            form.update { it.copy(amount = value) }
        }
    }

    fun setReceivedAmount(value: String) {
        if (value.isEmpty() || value.matches(Regex("^\\d*\\.?\\d*$"))) {
            form.update { it.copy(receivedAmount = value) }
        }
    }

    fun setCommissionPercent(value: String) {
        if (value.isEmpty() || value.matches(Regex("^\\d*\\.?\\d*$"))) {
            form.update { it.copy(commissionPercent = value) }
        }
    }

    fun setNote(value: String) {
        form.update { it.copy(note = value) }
    }

    fun transfer() {
        val current = form.value
        val fromId = current.fromAccountId ?: return
        val toId = current.toAccountId ?: return
        val amount = current.amount.toDoubleOrNull() ?: return
        if (amount <= 0) return

        viewModelScope.launch {
            form.update { it.copy(isSaving = true) }
            try {
                recordTransferUseCase(
                    fromAccountId = fromId,
                    toAccountId = toId,
                    amount = amount,
                    receivedAmount = current.receivedAmount.toDoubleOrNull(),
                    commissionPercent = current.commissionPercent.toDoubleOrNull(),
                    note = current.note.ifBlank { null },
                )
                form.update { it.copy(isSaving = false, isSaved = true) }
            } catch (_: Exception) {
                form.update { it.copy(isSaving = false) }
            }
        }
    }

    private data class TransferForm(
        val fromAccountId: String? = null,
        val toAccountId: String? = null,
        val amount: String = "",
        val receivedAmount: String = "",
        val commissionPercent: String = "",
        val note: String = "",
        val isSaving: Boolean = false,
        val isSaved: Boolean = false,
    )
}
