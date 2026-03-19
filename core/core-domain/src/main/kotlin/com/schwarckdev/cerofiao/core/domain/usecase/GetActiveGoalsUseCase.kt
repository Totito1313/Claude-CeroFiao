package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.domain.repository.SavingsGoalRepository
import com.schwarckdev.cerofiao.core.model.SavingsGoal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActiveGoalsUseCase @Inject constructor(
    private val savingsGoalRepository: SavingsGoalRepository
) {
    operator fun invoke(): Flow<List<SavingsGoal>> {
        return savingsGoalRepository.getActiveGoals()
    }
}
