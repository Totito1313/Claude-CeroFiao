package com.schwarckdev.cerofiao.core.domain.usecase

import com.schwarckdev.cerofiao.core.common.UuidGenerator
import com.schwarckdev.cerofiao.core.domain.repository.TransactionTitleRepository
import com.schwarckdev.cerofiao.core.model.TransactionTitle
import javax.inject.Inject

class SuggestCategoryByTitleUseCase @Inject constructor(
    private val transactionTitleRepository: TransactionTitleRepository,
) {
    suspend fun suggest(query: String): String? {
        if (query.isBlank()) return null
        val match = transactionTitleRepository.findMatch(query.trim())
        return match?.categoryId
    }

    suspend fun saveAssociation(title: String, categoryId: String) {
        val existing = transactionTitleRepository.findMatch(title.trim())
        if (existing != null) return
        transactionTitleRepository.insert(
            TransactionTitle(
                id = UuidGenerator.generate(),
                title = title.trim(),
                categoryId = categoryId,
                isExactMatch = true,
            ),
        )
    }
}
