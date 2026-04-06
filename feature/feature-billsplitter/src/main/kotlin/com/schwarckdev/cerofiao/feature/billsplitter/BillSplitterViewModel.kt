package com.schwarckdev.cerofiao.feature.billsplitter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schwarckdev.cerofiao.core.domain.repository.UserPreferencesRepository
import com.schwarckdev.cerofiao.core.domain.usecase.BuildRateTableUseCase
import com.schwarckdev.cerofiao.core.model.RateTable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

enum class SplitType {
    EQUAL, FIXED, PERCENTAGE
}

data class SplitParticipant(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val splitType: SplitType = SplitType.EQUAL,
    val fixedAmount: Double = 0.0,
    val percentage: Double = 0.0,
    val finalAmountBaseCurrency: Double = 0.0,
    val equivalentVes: Double = 0.0,
    val equivalentUsd: Double = 0.0,
)

data class BillSplitterState(
    val totalAmountStr: String = "",
    val baseCurrency: String = "USD",
    val participants: List<SplitParticipant> = emptyList(),
    val bcvRate: Double = 1.0,
    val totalAllocatedBase: Double = 0.0,
    val unallocatedBase: Double = 0.0,
)

@HiltViewModel
class BillSplitterViewModel @Inject constructor(
    private val buildRateTable: BuildRateTableUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(BillSplitterState())
    val state = _state.asStateFlow()

    private var rateTable: RateTable = RateTable.IDENTITY

    init {
        viewModelScope.launch {
            userPreferencesRepository.userPreferences.collect { prefs ->
                rateTable = buildRateTable.build(prefs.preferredRateSource)
                _state.update { it.copy(
                    baseCurrency = prefs.displayCurrencyCode.ifBlank { "USD" },
                    bcvRate = rateTable.rate("USD", "VES"),
                ) }
                recalculate()
            }
        }
    }

    fun onTotalAmountChanged(value: String) {
        if (value.isEmpty() || value.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) {
            _state.update { it.copy(totalAmountStr = value) }
            recalculate()
        }
    }

    fun onCurrencyChanged(currency: String) {
        _state.update { it.copy(baseCurrency = currency) }
        recalculate()
    }

    fun addParticipant(name: String) {
        if (name.isBlank()) return
        _state.update {
            it.copy(
                participants = it.participants + SplitParticipant(name = name)
            )
        }
        recalculate()
    }

    fun removeParticipant(id: String) {
        _state.update {
            it.copy(participants = it.participants.filter { p -> p.id != id })
        }
        recalculate()
    }

    fun updateParticipantType(id: String, type: SplitType) {
        _state.update { state ->
            state.copy(
                participants = state.participants.map { p ->
                    if (p.id == id) p.copy(splitType = type) else p
                }
            )
        }
        recalculate()
    }

    fun updateParticipantFixed(id: String, amount: Double) {
        _state.update { state ->
            state.copy(
                participants = state.participants.map { p ->
                    if (p.id == id) p.copy(fixedAmount = amount) else p
                }
            )
        }
        recalculate()
    }

    fun updateParticipantPercentage(id: String, percentage: Double) {
        _state.update { state ->
            state.copy(
                participants = state.participants.map { p ->
                    if (p.id == id) p.copy(percentage = percentage) else p
                }
            )
        }
        recalculate()
    }

    private fun recalculate() {
        val currentState = _state.value
        val totalAmount = currentState.totalAmountStr.toDoubleOrNull() ?: 0.0
        val baseCurrency = currentState.baseCurrency

        var fixedSum = 0.0
        var percentageSumAmounts = 0.0
        var equalCount = 0

        val calculatedParts = currentState.participants.map { p ->
            when (p.splitType) {
                SplitType.FIXED -> {
                    fixedSum += p.fixedAmount
                    p.copy(finalAmountBaseCurrency = p.fixedAmount)
                }
                SplitType.PERCENTAGE -> {
                    val amt = totalAmount * (p.percentage / 100.0)
                    percentageSumAmounts += amt
                    p.copy(finalAmountBaseCurrency = amt)
                }
                SplitType.EQUAL -> {
                    equalCount++
                    p
                }
            }
        }

        val remainingForEqual = (totalAmount - fixedSum - percentageSumAmounts).coerceAtLeast(0.0)
        val equalAmount = if (equalCount > 0) remainingForEqual / equalCount else 0.0

        val finalParticipants = calculatedParts.map { p ->
            val finalBase = if (p.splitType == SplitType.EQUAL) equalAmount else p.finalAmountBaseCurrency
            // Convert via RateTable — handles any base currency, not just USD/VES
            val usd = rateTable.convert(finalBase, baseCurrency, "USD")
            val ves = rateTable.convert(finalBase, baseCurrency, "VES")

            p.copy(
                finalAmountBaseCurrency = finalBase,
                equivalentUsd = usd,
                equivalentVes = ves,
            )
        }

        val totalAllocated = finalParticipants.sumOf { it.finalAmountBaseCurrency }
        val unallocated = totalAmount - totalAllocated

        _state.update {
            it.copy(
                participants = finalParticipants,
                totalAllocatedBase = totalAllocated,
                unallocatedBase = unallocated,
            )
        }
    }
}
