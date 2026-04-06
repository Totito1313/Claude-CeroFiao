package com.schwarckdev.cerofiao.feature.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.schwarckdev.cerofiao.core.domain.repository.AccountRepository
import com.schwarckdev.cerofiao.core.domain.repository.CategoryRepository
import com.schwarckdev.cerofiao.core.domain.repository.TransactionRepository
import com.schwarckdev.cerofiao.core.domain.usecase.DeleteTransactionUseCase
import com.schwarckdev.cerofiao.core.model.Account
import com.schwarckdev.cerofiao.core.model.Category
import com.schwarckdev.cerofiao.core.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionDetailUiState(
    val transaction: Transaction? = null,
    val account: Account? = null,
    val category: Category? = null,
    val transferToAccount: Account? = null,
    val isDeleted: Boolean = false,
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoryRepository,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<TransactionDetailRoute>()
    private val transactionId = route.transactionId

    val uiState: StateFlow<TransactionDetailUiState> = transactionRepository
        .getTransactionById(transactionId)
        .flatMapLatest { transaction ->
            if (transaction == null) {
                flowOf(TransactionDetailUiState())
            } else {
                val accountFlow = accountRepository.getAccountById(transaction.accountId)
                val categoryId = transaction.categoryId
                val categoryFlow = if (categoryId != null) {
                    flowOf(categoryRepository.getCategoryById(categoryId))
                } else {
                    flowOf(null)
                }
                val transferToAccountId = transaction.transferToAccountId
                val transferToFlow = if (transferToAccountId != null) {
                    accountRepository.getAccountById(transferToAccountId)
                } else {
                    flowOf(null)
                }

                combine(accountFlow, categoryFlow, transferToFlow) { account, category, transferTo ->
                    TransactionDetailUiState(
                        transaction = transaction,
                        account = account,
                        category = category,
                        transferToAccount = transferTo,
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TransactionDetailUiState(),
        )

    fun deleteTransaction() {
        viewModelScope.launch {
            deleteTransactionUseCase(transactionId)
        }
    }
}
