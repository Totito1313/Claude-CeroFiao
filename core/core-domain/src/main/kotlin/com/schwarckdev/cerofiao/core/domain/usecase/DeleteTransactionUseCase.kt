package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.model.TransactionType
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke(transactionId: String) {
        val transaction = transactionRepository.getTransactionByIdOnce(transactionId) ?: return

        // Reverse the balance effect on the source account
        val account = accountRepository.getAccountByIdOnce(transaction.accountId)
        if (account != null) {
            val reversedBalance = when (transaction.type) {
                TransactionType.INCOME -> account.balance - transaction.amount
                TransactionType.EXPENSE -> account.balance + transaction.amount
                TransactionType.TRANSFER -> account.balance + transaction.amount
            }
            accountRepository.updateBalance(account.id, reversedBalance)
        }

        // For transfers, also delete the linked transaction and reverse its balance
        val linkedId = transaction.transferLinkedId
        if (transaction.type == TransactionType.TRANSFER && linkedId != null) {
            val linkedTransaction = transactionRepository.getTransactionByIdOnce(linkedId)
            if (linkedTransaction != null) {
                val linkedAccount = accountRepository.getAccountByIdOnce(linkedTransaction.accountId)
                if (linkedAccount != null) {
                    val linkedReversedBalance = when (linkedTransaction.type) {
                        TransactionType.INCOME -> linkedAccount.balance - linkedTransaction.amount
                        TransactionType.EXPENSE -> linkedAccount.balance + linkedTransaction.amount
                        TransactionType.TRANSFER -> linkedAccount.balance + linkedTransaction.amount
                    }
                    accountRepository.updateBalance(linkedAccount.id, linkedReversedBalance)
                }
                transactionRepository.deleteTransaction(linkedId)
            }
        }

        // Soft-delete the original transaction
        transactionRepository.deleteTransaction(transactionId)
    }
}
