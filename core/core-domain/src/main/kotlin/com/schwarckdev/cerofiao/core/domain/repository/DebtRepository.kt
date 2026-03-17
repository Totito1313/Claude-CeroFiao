package com.schwarckdev.cerofiao.core.domain.repository

import com.schwarckdev.cerofiao.core.model.Debt
import com.schwarckdev.cerofiao.core.model.DebtPayment
import com.schwarckdev.cerofiao.core.model.DebtType
import kotlinx.coroutines.flow.Flow

interface DebtRepository {
    fun getAllDebts(): Flow<List<Debt>>
    fun getActiveDebtsByType(type: DebtType): Flow<List<Debt>>
    fun getDebtById(id: String): Flow<Debt?>
    fun getPaymentsForDebt(debtId: String): Flow<List<DebtPayment>>
    suspend fun insertDebt(debt: Debt)
    suspend fun updateDebt(debt: Debt)
    suspend fun deleteDebt(id: String)
    suspend fun insertPayment(payment: DebtPayment)
}
