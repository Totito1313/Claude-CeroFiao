package com.schwarckdev.cerofiao.core.data.repository

import com.schwarckdev.cerofiao.core.common.DateUtils
import com.schwarckdev.cerofiao.core.database.dao.DebtDao
import com.schwarckdev.cerofiao.core.database.entity.DebtPaymentEntity
import com.schwarckdev.cerofiao.core.database.mapper.toEntity
import com.schwarckdev.cerofiao.core.database.mapper.toModel
import com.schwarckdev.cerofiao.core.domain.repository.DebtRepository
import com.schwarckdev.cerofiao.core.model.Debt
import com.schwarckdev.cerofiao.core.model.DebtPayment
import com.schwarckdev.cerofiao.core.model.DebtType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebtRepositoryImpl @Inject constructor(
    private val debtDao: DebtDao,
) : DebtRepository {

    override fun getAllDebts(): Flow<List<Debt>> =
        debtDao.getAllDebts().map { list -> list.map { it.toModel() } }

    override fun getActiveDebtsByType(type: DebtType): Flow<List<Debt>> =
        debtDao.getActiveDebtsByType(type.name).map { list -> list.map { it.toModel() } }

    override fun getDebtById(id: String): Flow<Debt?> =
        debtDao.getDebtById(id).map { it?.toModel() }

    override fun getPaymentsForDebt(debtId: String): Flow<List<DebtPayment>> =
        debtDao.getPaymentsForDebt(debtId).map { list -> list.map { it.toModel() } }

    override suspend fun insertDebt(debt: Debt) {
        debtDao.insert(debt.toEntity())
    }

    override suspend fun updateDebt(debt: Debt) {
        debtDao.update(debt.toEntity())
    }

    override suspend fun deleteDebt(id: String) {
        debtDao.softDelete(id, DateUtils.now())
    }

    override suspend fun insertPayment(payment: DebtPayment) {
        val entity = DebtPaymentEntity(
            id = payment.id,
            debtId = payment.debtId,
            transactionId = payment.transactionId,
            amount = payment.amount,
            currencyCode = payment.currencyCode,
            exchangeRateToUsd = payment.exchangeRateToUsd,
            paidAt = payment.paidAt,
            note = payment.note,
            syncId = null,
            isDeleted = false,
        )
        debtDao.insertPayment(entity)
    }
}
