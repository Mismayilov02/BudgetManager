package com.mismayilov.domain.usecases.transaction

import com.mismayilov.domain.repositories.TransactionRepository
import com.mismayilov.domain.entities.local.TransactionModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(
        starDate: Long? = null,
        endDate: Long? = null
    ): Flow<List<TransactionModel>> {
        if (starDate == null || endDate == null) return transactionRepository.getTransactionWithLimit(15)
        return transactionRepository.getTransactionByTimeRange(starDate, endDate)
    }
}