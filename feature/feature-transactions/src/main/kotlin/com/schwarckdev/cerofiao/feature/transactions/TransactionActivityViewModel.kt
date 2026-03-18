package com.schwarckdev.cerofiao.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.domain.repository.TransactionLogRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.model.Transaction
import com.schwarckdev.cerofiao.core.model.TransactionLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class TransactionActivityViewModel @Inject constructor(
    private val transactionLogRepository: TransactionLogRepository,
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    val logs: StateFlow<List<TransactionLog>> = transactionLogRepository.getRecentLogs(50)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    fun restoreTransaction(log: TransactionLog) {
        viewModelScope.launch {
            val transaction = try {
                Json.decodeFromString<Transaction>(log.snapshotJson)
            } catch (_: Exception) {
                return@launch
            }
            transactionRepository.insertTransaction(transaction)
        }
    }
}
